package concurrent.io

import concurrent.ExecutionContexts
import concurrent.io.IO.Callback
import console.ConsoleForIO
import monix.eval.Task

import java.util.concurrent.atomic.AtomicReference
import scala.annotation.tailrec
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success, Try}

trait IO[+A] {
  def map[B](f: A => B): IO[B] = flatMap(a => IO.of(f(a)))

  def flatMap[B](f: A => IO[B]): IO[B] = FlatMap(this, f)

  def zip[B](that: IO[B]): IO[(A, B)] = Zip(this, that)

  def zipMap[B, C](that: IO[B])(f: (A, B) => C): IO[C] = zip(that).map(f.tupled)

  def bindTo(ec: ExecutionContext): IO[A] = BindTo(this, ec)

  def unsafeRunSync: A = IO.unsafeRunSync(this)

  def unsafeRunAsync(onComplete: Callback[A])(ec: ExecutionContext): Unit = IO.unsafeRunAsync(this)(onComplete)(ec)

  def unsafeRunToFuture(ec: ExecutionContext): scala.concurrent.Future[A] = {
    val p = scala.concurrent.Promise[A]()

    unsafeRunAsync(p.complete)(ec)

    p.future
  }
}

case class Pure[A](value: A) extends IO[A]
case class Error(error: Throwable) extends IO[Nothing]
case class Eval[A](value: () => A) extends IO[A]
case class Async[A](registerCallback: (ExecutionContext, Callback[A]) => Unit) extends IO[A]
case class BindTo[A](io: IO[A], ec: ExecutionContext) extends IO[A]
case class FlatMap[A, B](ioA: IO[A], f: A => IO[B]) extends IO[B] {
  type AType = A
  type BType = B
}
case class Zip[A, B](ioA: IO[A], ioB: IO[B]) extends IO[(A, B)] {
  type AType = A
  type BType = B
}

object IO {
  def apply[A](value: => A): IO[A] = Eval(() => value)

  def of[A](value: A): IO[A] = Pure(value)
  def failed(throwable: Throwable): IO[Nothing] = Error(throwable)


  type Callback[-A] = Try[A] => Unit

  def usingCallback[A](registerCallback: (ExecutionContext, Callback[A]) => Unit): IO[A] = Async(registerCallback)

  def fromFuture[A](fa: => scala.concurrent.Future[A]): IO[A] = usingCallback[A] { (ec, callback) =>
    fa.onComplete(callback)(ec)
  }

  def unsafeRunAsync[T](io: IO[T])(onComplete: Callback[T])(ec: ExecutionContext): Unit = {
    def execAsync(block: => Unit): Unit = ec.execute(() => block)

    io match {
      case Pure(value) => execAsync {
        onComplete(Success(value))
      }
      case Error(error) => execAsync {
        onComplete(Failure(error))
      }
      case Eval(delayedValue) => execAsync(onComplete(Try(delayedValue())))
      case Async(registerCallback) => registerCallback(ec, onComplete)
      case BindTo(nestedIO, newEc) => unsafeRunAsync(nestedIO)(onComplete)(newEc)
      case fm: FlatMap[_, T] =>
        val callback = (maybeValue: Try[fm.AType]) => maybeValue match {
          case Success(value) => execAsync {
            unsafeRunAsync(fm.f(value))(onComplete)(ec)
          }
          case Failure(error) => onComplete(Failure(error))
        }
        unsafeRunAsync(fm.ioA)(callback)(ec)
      case zipIo: Zip[_, _] =>
        val ioA: IO[zipIo.AType] = zipIo.ioA
        val ioB: IO[zipIo.BType] = zipIo.ioB

        val firstCompletedResult = new AtomicReference[Option[Either[Try[zipIo.AType], Try[zipIo.BType]]]](None)

        def completeWith(aResult: Try[zipIo.AType], bResult: Try[zipIo.BType]): Unit = {
          val result = for {
            a <- aResult
            b <- bResult
          } yield (a, b)

          onComplete(result)
        }

        @tailrec
        def completeA(aResult: Try[zipIo.AType]): Unit = firstCompletedResult.get() match {
          case Some(Right(bResult)) =>completeWith(aResult, bResult)
          case None =>
            if (!firstCompletedResult.compareAndSet(None, Some(Left(aResult)))) completeA(aResult)
          case _ =>
        }

        @tailrec
        def completeB(bResult: Try[zipIo.BType]): Unit = firstCompletedResult.get() match {
          case Some(Left(aResult)) => completeWith(aResult, bResult)
          case None =>
            if (!firstCompletedResult.compareAndSet(None, Some(Right(bResult)))) completeB(bResult)
          case _ =>
        }

        unsafeRunAsync(ioA)(completeA)(ec)
        unsafeRunAsync(ioB)(completeB)(ec)
    }
  }

  def unsafeRunSync[T](io: IO[T]): T = io match {
    case Pure(value) => value
    case Error(throwable) => throw throwable
    case Eval(delayedValue) => delayedValue()
    case fm: FlatMap[_, T] => unsafeRunSync(fm.f(unsafeRunSync(fm.ioA)))
    case zipIo: Zip[_, _] => (unsafeRunSync(zipIo.ioA), unsafeRunSync(zipIo.ioB))
    case Async(registerCallback) =>
      val p = scala.concurrent.Promise[T]()
      registerCallback(ExecutionContexts.currentThreadEc, p.complete)
      Await.result(p.future, Duration.Inf)
    case BindTo(nestedIO, _) => unsafeRunSync(nestedIO)
  }
}

object IOApp extends App {
  val console = new ConsoleForIO(ExecutionContexts.blocking)

  val task = IO {
    println("runnning")

    Thread.sleep(4000)

    1 + 1
  }

  def double(n: Int): IO[Int] = IO {
    println("Doubling...")

    n * 2
  }

  val calculation = for {
    combined <- task zip task
    (a, b) = combined
    c <- double(a + b)
    _ <- console.putStringLine(s"(a, b) is: ${(a, b)}. The result is: $c") // console puts this in the blocking EC
  } yield c

  println("The plan:" + calculation)

  // Everything is running synchronously on the main thread, even zip
  calculation.unsafeRunSync

  // Everything is running asynchronously on threads from the execution context
  calculation.unsafeRunAsync(r => println(s"The final result is: $r"))(ExecutionContexts.default)

  Thread.sleep(10000)
}
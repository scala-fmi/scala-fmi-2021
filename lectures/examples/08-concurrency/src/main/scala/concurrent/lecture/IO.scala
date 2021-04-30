package concurrent.lecture

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success, Try}

sealed trait IO[+A] {
  def map[B](f: A => B): IO[B] = flatMap(a => IO.of(f(a)))
  def flatMap[B](f: A => IO[B]): IO[B] = FlatMap(this, f)

  def zip[B](that: IO[B]): IO[(A, B)] = Zip(this, that)
  def zipMap[B, C](that: IO[B])(f: (A, B) => C): IO[C] = (this zip that).map(f.tupled)

  def unsafeRunAsync(callback: Try[A] => Unit)(ec: ExecutionContext): Unit = IO.unsafeRunAsync(this)(callback)(ec)
  def unsafeRunToFuture(ec: ExecutionContext): Future[A] = {
    val p = Promise[A]()

    unsafeRunAsync(p.complete)(ec)

    p.future
  }
  // filter, recover, ...
}

case class Pure[A](value: A) extends IO[A]
case class Error(error: Throwable) extends IO[Nothing]
case class Eval[A](value: () => A) extends IO[A]
case class FlatMap[A, B](io: IO[A], f: A => IO[B]) extends IO[B]
case class Zip[A, B](ioA: IO[A], ioB: IO[B]) extends IO[(A, B)]
case class Async[A](registerCallback: (ExecutionContext, Try[A] => Unit) => Unit) extends IO[A]

object IO {
  def apply[A](value: => A): IO[A] = Eval(() => value)
  def of[A](value: A): IO[A] = Pure(value)
  def failed(error: Throwable): IO[Nothing] = Error(error)

  def fromFuture[A](f: => Future[A]): IO[A] = Async { (ec, callback) =>
    f.onComplete(callback)(ec)
  }

  def unsafeRunAsync[T](io: IO[T])(callback: Try[T] => Unit)(ec: ExecutionContext): Unit = {
    def execAsync(block: => Unit): Unit = ec.execute(() => block)

    io match {
      case Pure(value) => execAsync {
        callback(Success(value))
      }
      case Error(error) => execAsync {
        callback(Failure(error))
      }
      case Eval(delayedValue) => execAsync {
        callback(Try(delayedValue()))
      }
      case Async(registerCallback) => registerCallback(ec, callback)
      case _ => ??? /* unfinished, see concurrent.io.IO instead */
    }
  }
}

object IOApp extends App {
  def calc1 = IO {
    1 + 1
  }

  def calc2 = IO {
    42
  }

  def double(n: Int) = IO {
    n * 2
  }

  val combinedCalculation = for {
    calcResults <- calc1 zip calc2
    (r1, r2) = calcResults
    doubled <- double(r1 + r2)
  } yield doubled

  println(combinedCalculation)
}
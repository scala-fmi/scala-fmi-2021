package referentialtransparancy

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object FutureReferentialTransparency extends App {
  def calc[T](expr: => T) = Future {
    Thread.sleep(4000)

    expr
  }

  val sum = for {
    (a, b) <- calc(42) zip calc(10)
  } yield a + b

  println {
    Await.result(sum, 5.seconds)
  }
}

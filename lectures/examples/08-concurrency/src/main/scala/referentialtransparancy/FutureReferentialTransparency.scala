package referentialtransparancy

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object FutureReferentialTransparency extends App {
  def calc[T](expr: => T) = Future {
    Thread.sleep(4000)

    expr
  }

  val futureA = calc(42)
  val futureB = calc(10)

  val sum = for {
    a <- futureA
    b <- futureB
  } yield a + b

  println {
    Await.result(sum, 5.seconds)
  }
}

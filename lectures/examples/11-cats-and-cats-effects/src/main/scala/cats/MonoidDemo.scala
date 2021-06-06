package cats

import cats.syntax.monoid._
import cats.instances.all._

object MonoidDemo extends App {
  1 |+| 2
  "ab".combineN(3)

  0.isEmpty

  Semigroup[Int].combineAllOption(List(1, 2, 3))
  Monoid[Int].combineAll(List(1, 2, 3))
}

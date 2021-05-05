package cats

import cats.instances.all._
import cats.syntax.monoid._

object CatsMonoidDemo extends App {
  (2, 3) |+| (4, 5) // (6, 8)
}

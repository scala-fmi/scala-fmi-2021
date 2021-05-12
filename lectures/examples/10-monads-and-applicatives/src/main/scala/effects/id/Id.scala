package effects.id

import effects.Monad
import effects.Monad.ops._

case class Id[A](value: A)

object Id {
  implicit val idMonad: Monad[Id] = new Monad[Id] {
    def unit[A](a: => A): Id[A] = Id(a)
    def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa.value)
  }
}

object IdDemo extends App {

  val r = for {
    a <- Id("Hello, ")
    b <- Id("World!")
  } yield a + b

  println(r.value)
}

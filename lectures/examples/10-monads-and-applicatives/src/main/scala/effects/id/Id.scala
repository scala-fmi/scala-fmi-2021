package effects.id

import effects.Monad

object Id {

  type Id[A] = A

  implicit val idMonad: Monad[Id] = new Monad[Id] {
    def unit[A](a: => A): Id[A] = a
    def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa)
  }
}

object IdDemo extends App {

  import effects.Monad.ops._
  import Id._

  val r = for {
    a <- "Hello, "
    b <- "World!"
  } yield a + b

  val a = "Hello, "
  val b = "World!"
  val r2 = a + b

  Monad[Id].map2(a, b)(_ + _)
  a + b

  println(r)
}

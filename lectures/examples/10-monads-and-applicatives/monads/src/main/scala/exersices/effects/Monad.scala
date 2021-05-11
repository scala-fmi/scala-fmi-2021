package exersices.effects

import effects.Monad

trait Monad[F[_]] {

  /*
  Exercise 1: Implement flatMap using compose and vice versa
   */

  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
  def unit[A](a: => A): F[A]

  def compose[A, B, C](f: A => F[B], g: B => F[C]): A => F[C] = ???

  /*
  Exercise 2: Implement the functions bellow
   */

  def map[A, B](fa: F[A])(f: A => B): F[B] = ???
  def flatten[A](ffa: F[F[A]]): F[A] = ???

  def zip[A, B](fa: F[A], fb: F[B]): F[(A, B)] = ???
  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = ???

  def sequence[A](xs: List[F[A]]): F[List[A]] = ???

}

object MonadTest extends App {
  val optionMonad: Monad[Option] = ???
}

object Monad {
  def apply[F[_]](implicit m: Monad[F]) = m

  def map[A, B, F[_]](fa: F[A])(f: A => B)(implicit m: Monad[F]) = m.map(fa)(f)
  def flatMap[A, B, F[_]](fa: F[A])(f: A => F[B])(implicit m: Monad[F]) = m.flatMap(fa)(f)

  def compose[A, B, C, F[_]](f: A => F[B], g: B => F[C])(implicit m: Monad[F]) = m.compose(f, g)
  def zip[A, B, F[_]](fa: F[A], fb: F[B])(implicit m: Monad[F]) = m.zip(fa, fb)
  def map2[A, B, C, F[_]](fa: F[A], fb: F[B])(f: (A, B) => C)(implicit m: Monad[F]) = m.map2(fa, fb)(f)
  def sequence[A, F[_]](ml: List[F[A]])(implicit m: Monad[F]) = m.sequence(ml)


  /*
  Exercise 3: Add Monad syntax to types that have a Monad instance - map, flatMap, flatten, sequence
   */

  object ops {

  }

  /*
  Exercise 4: Define Monad instances for types Option, State
   */

}

package effects

trait Applicative[F[_]] extends Functor[F] {

  // primitive combinators
  def map2[A,B,C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C]
  def unit[A](a: => A): F[A]

  // derived combinators
  def map[A, B](fa: F[A])(f: A => B): F[B] = map2(fa, unit(()))((a, _) => f(a))

  def apply[A, B](fab: F[A => B])(fa: F[A]): F[B] = map2(fab, fa)(_(_))

  def map3[A,B,C,D](fa: F[A], fb: F[B], fc: F[C])(f: (A, B, C) => D): F[D] = {
    val fbcd = map(fa)(f.curried)
    val fcd = apply(fbcd)(fb)
    apply(fcd)(fc)
  }

  /*
  The pattern is simple. We just curry the function
  we want to lift, pass the result to `unit`, and then `apply`
  as many times as there are arguments.
  Each call to `apply` is a partial application of the function
  */
  def map4[A,B,C,D,E](fa: F[A],
                      fb: F[B],
                      fc: F[C],
                      fd: F[D])(f: (A, B, C, D) => E): F[E] =
    apply(apply(apply(apply(unit(f.curried))(fa))(fb))(fc))(fd)

  def sequence[A](lfa: List[F[A]]): F[List[A]] =
    traverse(lfa)(fa => fa)

  def traverse[A,B](as: List[A])(f: A => F[B]): F[List[B]] =
    as.foldRight(unit(List[B]()))((a, mbs) => map2(f(a), mbs)(_ :: _))


}

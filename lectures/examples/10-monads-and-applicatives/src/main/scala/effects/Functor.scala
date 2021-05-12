package effects


trait Functor[F[_]]  {
  def map[A, B](fa: F[A])(f: A => B): F[B]

  /*
  A different view for functors
   */
  def lift[A, B](f: A => B): F[A] => F[B] =
    fa => map(fa)(f)

  /*
  Taken from the red book
   */
  def unzip[A,B](fab: F[(A, B)]): (F[A], F[B]) =
    (map(fab)(_._1), map(fab)(_._2))

  def codistribute[A,B](e: Either[F[A], F[B]]): F[Either[A, B]] =
    e match {
      case Left(fa) => map(fa)(Left(_))
      case Right(fb) => map(fb)(Right(_))
    }
}

object Functor {
  def apply[F[_]](implicit f: Functor[F]) = f

  trait FunctorOps {
    implicit class functorOps[F[_] : Functor, A](fa: F[A]) {
      def map[B](f: A => B): F[B] = Functor[F].map(fa)(f)
    }
  }
  object ops extends FunctorOps

  // Example implementation for Option
  implicit val optionFunctor: Functor[Option] = new Functor[Option] {
    def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa match {
      case None    => None
      case Some(a) => Some(f(a))
    }
  }
}

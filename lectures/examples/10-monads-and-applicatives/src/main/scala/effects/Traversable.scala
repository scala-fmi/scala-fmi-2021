package effects

import answers.Answers
import validation.RegistrationData


trait Traversable[F[_]] extends Functor[F] {
  def traverse[G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]] =
    sequence(map(fa)(f))

  def sequence[G[_]: Applicative, A](fga: F[G[A]]): G[F[A]] =
    traverse(fga)(ga => ga)

  def map[A,B](fa: F[A])(f: A => B): F[B] = {
    type Id[A] = A

    val idApplicative = new Applicative[Id] {
      def map2[A, B, C](fa: Id[A], fb: Id[B])(f: (A, B) => C): Id[C] = f(fa, fb)
      def unit[A](a: => A): Id[A] = a
    }

    traverse[Id, A, B](fa)(f)(idApplicative)
  }
}

object Traversable {
  def apply[F[_]](implicit a: Traversable[F]) = a

  implicit val listTraversable = new Traversable[List] {
    override def traverse[G[_], A, B](as: List[A])(f: A => G[B])(implicit G: Applicative[G]): G[List[B]] =
      as.foldRight(G.unit(List[B]()))((a, fbs) => G.map2(f(a), fbs)(_ :: _))
  }

  implicit val optionTraversable = new Traversable[Option] {
    override def traverse[G[_],A,B](oa: Option[A])(f: A => G[B])(implicit G: Applicative[G]): G[Option[B]] =
      oa match {
        case Some(a) => G.map(f(a))(Some(_))
        case None    => G.unit(None)
      }
  }

  case class Tree[+A](head: A, tail: List[Tree[A]])
  val treeTraverse = new Traversable[Tree] {
    override def traverse[M[_],A,B](ta: Tree[A])(f: A => M[B])(implicit M: Applicative[M]): M[Tree[B]] =
      M.map2(f(ta.head), listTraversable.traverse(ta.tail)(a => traverse(a)(f)))(Tree(_, _))
  }
}

object TraversableDemo extends App {
  import Traversable._
  import Applicative.optionApplicative
  import validation.FormValidatorNecApplicative
  import validation.FormValidatorNecApplicative.ValidationResult

  val listOfOptions: List[Option[Int]] = List(Some(1), Some(2), Some(3))

  println{
    Traversable[List].sequence(listOfOptions)
  }

  println{
    val optionOfValidated: Option[ValidationResult[RegistrationData]] = Some(FormValidatorNecApplicative.validateForm(
      username = "fake$Us#rname",
      password = "password"
    )(Answers.validatedApplicative))

    Traversable[Option].sequence(optionOfValidated)(Answers.validatedApplicative)
  }

  println {
    val optionOfValidated: Option[ValidationResult[RegistrationData]] = Some(
      FormValidatorNecApplicative.validateForm(
      username = "correctUsername",
      password = "Password123#"
    )(Answers.validatedApplicative))

    Traversable[Option].sequence(optionOfValidated)(Answers.validatedApplicative)
  }

  println {
    val optionOfValidated: Option[ValidationResult[RegistrationData]] = None

    Traversable[Option].sequence(optionOfValidated)(Answers.validatedApplicative)
  }
}

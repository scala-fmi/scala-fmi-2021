package cats

import cats.data.ValidatedNec
import cats.instances.all._
import cats.syntax.applicative._
import cats.syntax.apply._
import cats.syntax.option._
import cats.syntax.traverse._
import user.UserRegistration._
import user.{RegistrationForm, RegistrationFormError, User}

object ApplyApplicativeTraverseDemo extends App {
  // Apply

  val ex = (1.some, none[Int], 3.some).tupled
  (1.some, 2.some, 3.some).mapN((x, y, z) => x + y + z)
  (1.some, 2.some, 3.some).traverseN((x, y, z) => List(x, y, z))

  1.some.map2(2.some)(_ + _)

  val registrationForm = RegistrationForm(
    "Tanya",
    "tanya@gmail.com",
    "dfdsdsfdsfdsfdsf"
  )

  val validatedUser = (
    validateName(registrationForm.name),
    validateEmail(registrationForm.email),
    validatePassword(registrationForm.password)
  ).mapN(User.apply)

  println(validatedUser)

  // Applicative

  val fourtyTwo = 42.pure[Option]

  val sum3 = (a: Int, b: Int, c: Int) => a + b + c
  val summed = sum3.curried.pure[Option] <*> 1.some <*> 10.some <*> 100.some

  import cats.syntax.flatMap._
  println {
    1.some >> none[Int] >> 100.some
  }

  type RegistrationFormValidated[A] = ValidatedNec[RegistrationFormError, A]

  (User.apply _).curried.pure[RegistrationFormValidated] <*>
    validateName(registrationForm.name) <*>
    validateEmail(registrationForm.email) <*>
    validatePassword(registrationForm.password)

  // Collects all the errors, but returns only the valid name in the end
  val validated = validatePassword(registrationForm.password) <*
    validateEmail(registrationForm.email) <*
    validateName(registrationForm.name)

  println(validated)

  println {
    fourtyTwo.replicateA(5)
  }

  // Traverse

  List(1.some, 2.some, 3.some, 4.some).sequence
  List(1, 2, 3, 4).traverse(_.some)

  val items = List(10.some, 2.some, none, 42.some, 100.some)
  val maxItem = items.sequence.flatMap(_.maxOption).getOrElse(Int.MaxValue)
}

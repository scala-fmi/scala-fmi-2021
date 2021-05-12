package validation

import effects.{Monad => MyMonad}

object FormValidatorNec {
  import cats.data._
  import cats.data.Validated._
  import cats.implicits._

  type ValidationResult[A] = ValidatedNec[DomainValidation, A]

  private def validateUserName(userName: String): ValidationResult[String] =
    if (userName.matches("^[a-zA-Z0-9]+$")) userName.validNec else UsernameHasSpecialCharacters.invalidNec

  private def validatePassword(password: String): ValidationResult[String] =
    if (password.matches("(?=^.{10,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")) password.validNec
    else PasswordDoesNotMeetCriteria.invalidNec


  implicit val validatedMonad = new MyMonad[ValidationResult] {
    def flatMap[A, B](fa: ValidationResult[A])(f: A => ValidationResult[B]): ValidationResult[B] = fa match {
      case Valid(a) => f(a)
      case Invalid(_) => fa.asInstanceOf[ValidationResult[B]]
    }

    def unit[A](a: => A): ValidationResult[A] = Valid(a)
  }

  def validateForm(username: String, password: String): ValidationResult[RegistrationData] = {
    validatedMonad.map2(
      validateUserName(username),
      validatePassword(password)
    )(RegistrationData.apply)
  }
}

object FormValidatorNecDemo extends App {
  println {
    FormValidatorNec.validateForm(
      username = "fake$Us#rname",
      password = "password"
    )
  }
}

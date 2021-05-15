package validation

import cats.data.Validated.{Invalid, Valid}
import effects.Applicative
import validation.FormValidatorNecApplicative.ValidationResult

object FormValidatorNecApplicative {
  import cats.data._
  import cats.data.Validated._
  import cats.implicits._

  type ValidationResult[A] = ValidatedNec[DomainValidation, A]

  private def validateUserName(userName: String): ValidationResult[String] =
    if (userName.matches("^[a-zA-Z0-9]+$")) userName.validNec else UsernameHasSpecialCharacters.invalidNec

  private def validatePassword(password: String): ValidationResult[String] =
    if (password.matches("(?=^.{10,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")) password.validNec
    else PasswordDoesNotMeetCriteria.invalidNec

  def validateForm(username: String, password: String)(implicit validatedApplicative: Applicative[ValidationResult]): ValidationResult[RegistrationData] = {
    validatedApplicative.map2(
      validateUserName(username),
      validatePassword(password)
    )(RegistrationData.apply)
  }
}

object FormValidatorNecApplicativeDemo extends App {
  implicit val validatedApplicative: Applicative[ValidationResult] = new Applicative[ValidationResult] {
    def map2[A, B, C](fa: ValidationResult[A], fb: ValidationResult[B])(f: (A, B) => C): ValidationResult[C] =
      (fa, fb) match {
        case (Valid(a), Valid(b)) => Valid(f(a, b))
        case (Valid(_), Invalid(nec)) => Invalid(nec)
        case (Invalid(nec), Valid(_)) => Invalid(nec)
        case (Invalid(nec1), Invalid(nec2)) => Invalid(nec1 ++ nec2)
      }

    def unit[A](a: => A): ValidationResult[A] = Valid(a)
  }

  println{
    FormValidatorNecApplicative.validateForm(
      username = "fake$Us#rname",
      password = "password"
    )
  }
}

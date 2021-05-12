package validation

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
  implicit val validatedApplicative: Applicative[ValidationResult] = ???

  println{
    FormValidatorNecApplicative.validateForm(
      username = "fake$Us#rname",
      password = "password"
    )
  }
}

package validation

import effects.Applicative

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


  implicit val validatedApplicative: Applicative[ValidationResult] = ???

  def validateForm(username: String, password: String): ValidationResult[RegistrationData] = {
    validatedApplicative.map2(
      validateUserName(username),
      validatePassword(password)
    )(RegistrationData.apply)
  }
}

object FormValidatorNecApplicativeDemo extends App {
  println{
    FormValidatorNecApplicative.validateForm(
      username = "fake$Us#rname",
      password = "password"
    )
  }
}

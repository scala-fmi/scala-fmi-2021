package validation

sealed trait FormValidator {
  def validateUserName(userName: String): Either[DomainValidation, String] =
    Either.cond(
      userName.matches("^[a-zA-Z0-9]+$"),
      userName,
      UsernameHasSpecialCharacters
    )

  def validatePassword(password: String): Either[DomainValidation, String] =
    Either.cond(
      password.matches("(?=^.{10,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$"),
      password,
      PasswordDoesNotMeetCriteria
    )

  def validateForm(username: String, password: String): Either[DomainValidation, RegistrationData] = {

    for {
      validatedUserName <- validateUserName(username)
      validatedPassword <- validatePassword(password)
    } yield RegistrationData(validatedUserName, validatedPassword)
  }

}

object FormValidator extends FormValidator

object FormValidationDemo extends App {
  println{
    FormValidator.validateForm(
      username = "fake#$rname",
      password = "password"
    )
  }
}

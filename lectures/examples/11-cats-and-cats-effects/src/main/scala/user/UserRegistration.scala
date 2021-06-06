package user

import cats.data.ValidatedNec
import cats.syntax.validated._

sealed trait RegistrationFormError
case object NameIsEmpty extends RegistrationFormError
case class InvalidEmail(email: String) extends RegistrationFormError
case object PasswordTooShort extends RegistrationFormError

case class RegistrationForm(
  name: String,
  email: String,
  password: String,
)

case class User(
  name: String,
  email: Email,
  passwordHash: String,
)

case class Email(user: String, domain: String)
object Email {
  def unapply(email: String): Option[(String, String)] = email.split("@") match {
    case Array(user, domain) if user.nonEmpty && domain.nonEmpty =>
      Some((user, domain))
    case _ => None
  }
}

object UserRegistration {
  def validateName(name: String): ValidatedNec[RegistrationFormError, String] = {
    if (name.nonEmpty) name.validNec
    else NameIsEmpty.invalidNec
  }

  def validateEmail(email: String): ValidatedNec[RegistrationFormError, Email] = email match {
    case Email(user, domain) => Email(user, domain).validNec
    case _ => InvalidEmail(email).invalidNec
  }

  def validatePassword(password: String): ValidatedNec[RegistrationFormError, String] = {
    if (password.length > 8) password.validNec.map(p => s"hashed $p")
    else PasswordTooShort.invalidNec
  }
}

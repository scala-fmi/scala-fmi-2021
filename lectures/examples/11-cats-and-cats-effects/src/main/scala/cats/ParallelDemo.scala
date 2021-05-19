package cats

import cats.AlternativeUserRegistration._
import cats.data.{EitherNec, ValidatedNec}
import cats.instances.all._
import cats.syntax.apply._
import cats.syntax.either._
import cats.syntax.parallel._
import cats.syntax.validated._
import user.Email

object ParallelDemo extends App {
  def registerUser(token: String)(name: String, email: String): EitherNec[String, User] = for {
    name <- verifyUserToken(token)
    user <- (
      validateName(name).toValidated,
      validateEmail(email).toValidated
    ).mapN(User.apply).toEither
  } yield user

//  (List(1, 2, 3), List(10, 20, 30), List(100, 200, 300)).mapN((x, y, z) => x + y + z)
//  (List(1, 2, 3), List(10, 20, 30), List(100, 200, 300)).parMapN((x, y, z) => x + y + z)
}

object AlternativeUserRegistration {
  def verifyUserToken(token: String): EitherNec[String, String] = {
    if (token.length > 10) token.rightNec
    else s"Invalid token: $token".leftNec
  }

  def validateName(name: String): EitherNec[String, String] = {
    if (name.nonEmpty) name.rightNec
    else "Name is empty".leftNec
  }

  def validateEmail(email: String): EitherNec[String, Email] = email match {
    case Email(user, domain) => Email(user, domain).rightNec
    case _ => s"Email is invalid: $email".leftNec
  }
}

case class User(name: String, email: Email)
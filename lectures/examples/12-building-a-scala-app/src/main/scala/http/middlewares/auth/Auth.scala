package http.middlewares.auth

import cats.data.{Kleisli, OptionT}
import cats.effect.IO
import cats.implicits._
import org.http4s.dsl.io._
import org.http4s.server.AuthMiddleware
import org.http4s.{AuthedRoutes, Request, Response, ResponseCookie}

object Auth {


  def login(userId: Int): IO[Response[IO]] =
    Ok("Logged in!").map(_.addCookie(ResponseCookie("authcookie", userId.toString)))


  def retrieveUser(id: Int) =
    UserDatabase(id).map(_.toRight("User not found"))

  val authUser: Kleisli[IO, Request[IO], Either[String, User]] =
    Kleisli { request =>
      val eitherUserId = for {
        cookie <- request.cookies.find(_.name == "authcookie").toRight("Cookie parsing error")
        userId <- Either.catchOnly[NumberFormatException](cookie.content.toInt).leftMap(_.toString)
      } yield userId

      eitherUserId.fold(
        error => IO.pure(Left(error)),
        retrieveUser
      )
    }


  val onFailure: AuthedRoutes[String, IO] = Kleisli(req => OptionT.liftF(Forbidden(req.context)))
  val middleware: AuthMiddleware[IO, User] = AuthMiddleware(authUser, onFailure)

}

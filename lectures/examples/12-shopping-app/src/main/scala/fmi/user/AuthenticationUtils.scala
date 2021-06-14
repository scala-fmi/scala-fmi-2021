package fmi.user

import cats.data.{EitherT, Kleisli, OptionT}
import cats.effect.IO
import fmi.infrastructure.CryptoService
import org.http4s.dsl.io._
import org.http4s.headers.Cookie
import org.http4s.server.AuthMiddleware
import org.http4s.{AuthedRoutes, Request, Response, ResponseCookie}

case class AuthenticatedUser(id: UserId, role: UserRole.Value)

class AuthenticationUtils(cryptoService: CryptoService, usersDao: UsersDao) {
  def responseWithUser(userId: UserId): IO[Response[IO]] = {
    val encryptedUser = cryptoService.encrypt(userId.email)
    Ok().map(_.addCookie(ResponseCookie("loggedUser", encryptedUser)))
  }

  def removeUser: IO[Response[IO]] = {
    Ok().map(_.removeCookie("loggedUser"))
  }

  private val authUser: Kleisli[IO, Request[IO], Either[String, AuthenticatedUser]] = Kleisli { request =>
    val userId = for {
      header <- request.headers.get[Cookie].toRight("Cookie parsing error")
      cookie <- header.values.toList.find(_.name == "loggedUser").toRight("Not authenticated")
      email <- cryptoService.decrypt(cookie.content).toRight("Cookie invalid")
    } yield UserId(email)

    (for {
      userId <- EitherT.fromEither[IO](userId)
      user <- EitherT(usersDao.retrieveUser(userId).map(_.toRight("User not found")))
    } yield AuthenticatedUser(user.id, user.role)).value
  }

  private val onFailure: AuthedRoutes[String, IO] = Kleisli(req => OptionT.liftF(Forbidden(req.context)))

  val authMiddleware: AuthMiddleware[IO, AuthenticatedUser] = AuthMiddleware(authUser, onFailure)
}

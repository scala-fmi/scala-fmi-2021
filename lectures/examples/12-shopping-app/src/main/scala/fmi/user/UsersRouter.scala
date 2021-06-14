package fmi.user

import cats.effect.IO
import io.circe.Codec
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.{deriveConfiguredCodec, deriveUnwrappedCodec}
import io.circe.generic.semiauto.deriveCodec
import org.http4s.dsl.io._
import org.http4s.{AuthedRoutes, HttpRoutes}

class UsersRouter(usersService: UsersService, authorizationUtils: AuthenticationUtils) {
  import UsersJsonCodecs._
  import org.http4s.circe.CirceEntityCodec._

  def nonAuthenticatedRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ POST -> Root / "users" =>
      for {
        registrationForm <- req.as[UserRegistrationForm]
        maybeUser <- usersService.registerUser(registrationForm)
        response <- maybeUser.fold(errors => BadRequest(errors), _ => Ok())
      } yield response

    case req @ POST -> Root / "login" =>
      for {
        userLogin <- req.as[UserLogin]
        maybeUser <- usersService.login(userLogin)
        response <- maybeUser
          .map { user => authorizationUtils.responseWithUser(user.id) }
          .getOrElse(Forbidden())
      } yield response
  }

  def authenticatedRoutes: AuthedRoutes[AuthenticatedUser, IO] = AuthedRoutes.of[AuthenticatedUser, IO] {
      case GET -> Root / "user" as user => Ok(user)
      case req @ POST -> Root / "logout" as _ => authorizationUtils.removeUser
  }
}

object UsersJsonCodecs {
  implicit val configuration: Configuration = Configuration.default.withDiscriminator("type")

  implicit val userRegistrationFormCodec: Codec[UserRegistrationForm] = deriveCodec

  implicit val userIdCodec: Codec[UserId] = deriveUnwrappedCodec
  implicit val userLoginCodec: Codec[UserLogin] = deriveCodec
  implicit val userRoleCodec: Codec[UserRole.Value] = Codec.codecForEnumeration(UserRole)
  implicit val authenticatedUserCodec: Codec[AuthenticatedUser] = deriveCodec

  implicit val registrationFormErrorCodec: Codec[RegistrationFormError] = deriveConfiguredCodec
  implicit val registrationErrorCodec: Codec[RegistrationError] = deriveConfiguredCodec
}

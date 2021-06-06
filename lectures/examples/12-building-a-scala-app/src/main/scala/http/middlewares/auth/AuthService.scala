package http.middlewares.auth

import cats.effect.IO
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.{AuthedRoutes, HttpRoutes}

object AuthService {

  object UserIdQueryParam extends QueryParamDecoderMatcher[Int]("userId")

  val unprotectedRoutes = HttpRoutes.of[IO] {
    case POST -> Root / "login" :? UserIdQueryParam(userId) =>
      Auth.login(userId)
  }

  val authedRoutes: AuthedRoutes[User, IO] =
    AuthedRoutes.of {
      case GET -> Root / "welcome" as user =>
        Ok(s"Welcome, ${user.name}")
    }

  val httpApp = Router(
    "/" -> unprotectedRoutes,
    "/protected" -> Auth.middleware(authedRoutes)
  ).orNotFound
}

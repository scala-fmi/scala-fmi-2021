package fmi.user

import cats.effect.IO
import cats.effect.kernel.Resource
import fmi.infrastructure.CryptoService
import fmi.infrastructure.db.DoobieDatabase.DbTransactor
import org.http4s.{AuthedRoutes, HttpRoutes}
import org.http4s.server.AuthMiddleware

case class UsersModule(
  usersDao: UsersDao,
  usersService: UsersService,
  authMiddleware: AuthMiddleware[IO, AuthenticatedUser],
  routes: HttpRoutes[IO],
  authenticatedRoutes: AuthedRoutes[AuthenticatedUser, IO]
)

object UsersModule {
  def apply(dbTransactor: DbTransactor, cryptoService: CryptoService): Resource[IO, UsersModule] = {
    val usersDao = new UsersDao(dbTransactor)
    val usersService = new UsersService(usersDao)
    val authenticationUtils = new AuthenticationUtils(cryptoService, usersDao)
    val usersRouter = new UsersRouter(usersService, authenticationUtils)

    Resource.pure(UsersModule(
      usersDao,
      usersService,
      authenticationUtils.authMiddleware,
      usersRouter.nonAuthenticatedRoutes,
      usersRouter.authenticatedRoutes
    ))
  }
}

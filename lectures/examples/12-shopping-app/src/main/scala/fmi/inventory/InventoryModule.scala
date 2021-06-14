package fmi.inventory

import cats.effect.IO
import cats.effect.kernel.Resource
import fmi.infrastructure.db.DoobieDatabase.DbTransactor
import fmi.user.AuthenticatedUser
import org.http4s.{AuthedRoutes, HttpRoutes}

case class InventoryModule(
  productDao: ProductDao,
  productStockDao: ProductStockDao,
  routes: HttpRoutes[IO],
  authenticatedRoutes: AuthedRoutes[AuthenticatedUser, IO]
)

object InventoryModule {
  def apply(dbTransactor: DbTransactor): Resource[IO, InventoryModule] = {
    val productDao = new ProductDao(dbTransactor)
    val productStockDao = new ProductStockDao(dbTransactor)
    val inventoryRouter = new InventoryRouter(productDao, productStockDao)

    Resource.pure(InventoryModule(
      productDao,
      productStockDao,
      inventoryRouter.nonAuthenticatedRoutes,
      inventoryRouter.authenticatedRoutes
    ))
  }
}

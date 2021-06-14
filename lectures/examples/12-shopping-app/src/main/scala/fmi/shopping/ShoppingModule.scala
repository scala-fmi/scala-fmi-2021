package fmi.shopping

import cats.effect.IO
import cats.effect.kernel.Resource
import fmi.infrastructure.db.DoobieDatabase.DbTransactor
import fmi.inventory.ProductStockDao
import fmi.user.AuthenticatedUser
import org.http4s.AuthedRoutes

case class ShoppingModule(
  orderDao: OrderDao,
  shippingRouter: OrderService,
  authenticatedRoutes: AuthedRoutes[AuthenticatedUser, IO]
)

object ShoppingModule {
  def apply(
   dbTransactor: DbTransactor,
   productStockDao: ProductStockDao,
  ): Resource[IO, ShoppingModule] = {
    val orderDao = new OrderDao(dbTransactor)
    val orderService = new OrderService(dbTransactor)(productStockDao, orderDao)
    val shippingRouter = new ShippingRouter(orderService)

    Resource.pure(
      ShoppingModule(orderDao, orderService, shippingRouter.authenticatedRoutes)
    )
  }
}

package fmi.shopping

import cats.effect.IO
import cats.implicits._
import doobie.implicits._
import fmi.infrastructure.db.DoobieDatabase.DbTransactor
import fmi.inventory.{InventoryAdjustment, NotEnoughStockAvailable, NotEnoughStockAvailableException, ProductStockDao}
import fmi.user.UserId

class OrderService(dbTransactor: DbTransactor)(productStockDao: ProductStockDao, orderDao: OrderDao) {
  // TODO: validate shopping cart has positive quantities
  def placeOrder(user: UserId, shoppingCart: ShoppingCart): IO[Either[NotEnoughStockAvailable.type, Order]] = for {
    orderId <- OrderId.generate
    placingTimestamp <- IO.realTimeInstant

    order = Order(orderId, user, shoppingCart.orderLines, placingTimestamp)

    maybeOrder <- transactOrder(shoppingCart.toInventoryAdjustment, order)
  } yield maybeOrder

  private def transactOrder(inventoryAdjustment: InventoryAdjustment, order: Order)
  : IO[Either[NotEnoughStockAvailable.type, Order]] = {
    val transaction =
      productStockDao.applyInventoryAdjustmentAction(inventoryAdjustment) *>
        orderDao.placeOrder(order)

    transaction
      .transact(dbTransactor)
      .map(_.asRight[NotEnoughStockAvailable.type])
      .recover {
        case NotEnoughStockAvailableException => NotEnoughStockAvailable.asLeft
      }
  }
}

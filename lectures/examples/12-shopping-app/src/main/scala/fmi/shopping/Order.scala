package fmi.shopping

import cats.effect.IO
import fmi.inventory.{ProductSku, ProductStockAdjustment}
import fmi.user.UserId

import java.time.Instant
import java.util.UUID

case class Order(orderId: OrderId, user: UserId, orderLines: List[OrderLine], placingTimestamp: Instant)
case class OrderId(id: String) extends AnyVal

case class OrderLine(product: ProductSku, quantity: Int) {
  def toProductStockAdjustment = ProductStockAdjustment(product, -quantity)
}

object OrderId {
  def generate: IO[OrderId] = IO(OrderId(UUID.randomUUID().toString))
}

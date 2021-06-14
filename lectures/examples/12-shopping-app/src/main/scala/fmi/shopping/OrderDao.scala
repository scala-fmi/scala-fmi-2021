package fmi.shopping

import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.util.meta.LegacyInstantMetaInstance
import fmi.infrastructure.db.DoobieDatabase.DbTransactor


class OrderDao(dbTransactor: DbTransactor) extends LegacyInstantMetaInstance {
  def placeOrder(order: Order): ConnectionIO[Order] = {
    val insertOrder = sql"""
      INSERT INTO "order" (id, user_id, placing_timestamp)
      VALUES (${order.orderId}, ${order.user}, ${order.placingTimestamp})
    """

    val insertOrderLine = {
      val orderLinesInsert = """
        INSERT INTO order_line(order_id, sku, quantity)
        VALUES(?, ?, ?)
      """

      Update[(OrderId, OrderLine)](orderLinesInsert)
        .updateMany(order.orderLines.map(ol => (order.orderId, ol)))
    }

    (insertOrder.update.run *> insertOrderLine).as(order)
  }
}

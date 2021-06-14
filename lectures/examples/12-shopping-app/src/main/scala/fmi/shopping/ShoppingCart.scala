package fmi.shopping

import fmi.inventory.InventoryAdjustment

case class ShoppingCart(orderLines: List[OrderLine] = List.empty) {
  def add(orderLine: OrderLine): ShoppingCart = {
    ShoppingCart(orderLine :: orderLines)
  }

  def toInventoryAdjustment: InventoryAdjustment =
    InventoryAdjustment(orderLines.map(_.toProductStockAdjustment))
}

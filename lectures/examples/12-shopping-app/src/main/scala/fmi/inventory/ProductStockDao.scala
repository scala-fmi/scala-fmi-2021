package fmi.inventory

import cats.effect.IO
import cats.implicits._
import doobie._
import doobie.implicits._
import fmi.infrastructure.db.DoobieDatabase.DbTransactor

class ProductStockDao(dbTransactor: DbTransactor) {
  def retrieveAllAvailableStock: IO[List[ProductStock]] = {
    sql"""
      SELECT sku, quantity
      FROM product_stock
      WHERE quantity > 0
    """
      .query[ProductStock]
      .to[List]
      .transact(dbTransactor)
  }

  def retrieveStockForProduct(sku: ProductSku): IO[Option[ProductStock]] = {
    sql"""
      SELECT sku, quantity
      FROM product_stock
      WHERE sku = $sku
    """
      .query[ProductStock]
      .option
      .transact(dbTransactor)
  }

  def applyInventoryAdjustment(inventoryAdjustment: InventoryAdjustment): IO[AdjustmentResult] = {
    applyInventoryAdjustmentAction(inventoryAdjustment)
      .transact(dbTransactor)
      .as(SuccessfulAdjustment: AdjustmentResult)
      .recover {
        case NotEnoughStockAvailableException => NotEnoughStockAvailable
      }
  }

  def applyInventoryAdjustmentAction(inventoryAdjustment: InventoryAdjustment): ConnectionIO[Unit]= {
    // TODO: Replace by a single query
    inventoryAdjustment
      .adjustments
      // Sorting allows us to avoid deadlock
      .sortBy(_.product.sku)
      .map(adjustStockForProduct)
      .sequence
      .void
  }

  private def adjustStockForProduct(adjustment: ProductStockAdjustment): ConnectionIO[Unit] = {
    val addQuery = sql"""
      INSERT INTO product_stock as ps (sku, quantity)
      VALUES (${adjustment.product}, ${adjustment.adjustmentQuantity})
      ON CONFLICT (sku) DO UPDATE SET
      quantity = ps.quantity + EXCLUDED.quantity
    """
    val substractQuery = sql"""
      UPDATE product_stock
      SET quantity = quantity + ${adjustment.adjustmentQuantity}
      WHERE sku = ${adjustment.product} AND quantity + ${adjustment.adjustmentQuantity} >= 0
    """

    val query = if (adjustment.adjustmentQuantity >= 0) addQuery else substractQuery

    query
      .update
      .run
      .map(updatedRows => updatedRows == 1)
      .ifM[Unit](().pure[ConnectionIO], NotEnoughStockAvailableException.raiseError[ConnectionIO, Unit])
  }
}

case object NotEnoughStockAvailableException extends Exception

sealed trait AdjustmentResult
case object SuccessfulAdjustment extends AdjustmentResult
case object NotEnoughStockAvailable extends AdjustmentResult

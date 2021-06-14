package fmi.inventory

import cats.effect.IO
import cats.implicits._
import doobie.implicits._
import fmi.infrastructure.db.DoobieDatabase.DbTransactor

class ProductDao(dbTransactor: DbTransactor) {
  def retrieveProduct(sku: ProductSku): IO[Option[Product]] = {
    sql"""
      SELECT sku, name, description, weight_in_grams
      FROM product
      WHERE sku = $sku
    """
      .query[Product]
      .option
      .transact(dbTransactor)
  }

  def addProduct(product: Product): IO[Unit] = {
    sql"""
      INSERT INTO product as p (sku, name, description, weight_in_grams)
      VALUES (${product.sku}, ${product.name}, ${product.description}, ${product.weightInGrams})
      ON CONFLICT (sku) DO UPDATE SET
      name = EXCLUDED.name,
      description = EXCLUDED.description,
      weight_in_grams = EXCLUDED.weight_in_grams
    """
      .update
      .run
      .void
      .transact(dbTransactor)
  }
}

package fmi.inventory

case class ProductSku(sku: String)
case class Product(sku: ProductSku, name: String, description: String, weightInGrams: Int)

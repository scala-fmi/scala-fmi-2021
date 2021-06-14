package fmi.inventory

case class ProductStock(product: ProductSku, quantity: Int)

case class ProductStockAdjustment(product: ProductSku, adjustmentQuantity: Int)
case class InventoryAdjustment(adjustments: List[ProductStockAdjustment])

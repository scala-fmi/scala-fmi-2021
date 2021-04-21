package product

case class Product(name: String, kind: String)
case class Verification(quality: Int)

object ProductFactory {
  def produceProduct(kind: String): Product = {
    val productId = Thread.currentThread().getId

    println(s"Producing product $productId...")
    Thread.sleep(2000)
    println(s"Product produced, $productId")

    Product(s"Product $productId", kind)
  }

  def verifyProduct(product: Product): Verification = {
    val threadId = Thread.currentThread().getId

    println(s"Verifying product ${product.name}...")
    Thread.sleep(2000)
    println(s"Product verified, thread: $threadId")

    Verification(threadId.hashCode)
  }
}
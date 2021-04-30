package callbacks

import product.{Product, ProductFactory, Verification}

import java.util.concurrent.{Executor, Executors}

object Callbacks {
  val threadPool: Executor = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors)
  def execute(work: => Any): Unit = threadPool.execute(() => work)

  def produceProduct(onComplete: Product => Unit): Unit = execute {
    val product = ProductFactory.produceProduct("Book")

    execute(onComplete(product))
  }

  def produce2Products(onComplete: (Product, Product) => Unit): Unit = {
    var firstProduct: Option[Product] = None

    def callback(newProduct: Product): Unit = this.synchronized {
      firstProduct match {
        case None =>
          firstProduct = Some(newProduct)
        case Some(existingProduct) =>
          onComplete(existingProduct, newProduct)
      }
    }

    produceProduct(callback)
    produceProduct(callback)
  }

  def main(args: Array[String]): Unit = execute {
    produce2Products((p1, p2) => println((p1, p2)))
  }

  def verifyProduct(product: Product)(onVerified: Verification => Unit): Unit = execute {
    val verification = ProductFactory.verifyProduct(product)

    execute(onVerified(verification))
  }

  def produceInPipeline(callback: (List[Product], List[Verification]) => Unit): Unit = {
    // Callback hell
    produceProduct { a =>
      verifyProduct(a) { aVerification =>
        produceProduct { b =>
          verifyProduct(b) { bVerification =>
            callback(List(a, b), List(aVerification, bVerification))
          }
        }
      }
    }
  }
}

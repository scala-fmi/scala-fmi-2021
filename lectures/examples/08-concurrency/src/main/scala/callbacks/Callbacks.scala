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

  def produce2Products(onComplete: (Product, Product) => Unit): Unit = ???

  def main(args: Array[String]): Unit = execute {
    produceProduct(println)
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

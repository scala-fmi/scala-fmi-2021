package threads

class CalculationSetting(var shouldImproveMore: Boolean)

object ThreadsSharingData extends App {
  var calculationSetting = new CalculationSetting(true)
  @volatile var improveCalculation = true

  val thread = new Thread(() => {
    var i = 0L

    while (improveCalculation || calculationSetting.shouldImproveMore) {
      i += 1
    }

    println(s"Thread exiting: $i")
  })

  thread.start()

  println("Main going to sleep...")
  Thread.sleep(1000)
  println("Main waking up...")

  calculationSetting.shouldImproveMore = false
  improveCalculation = false

  thread.join()

  println("Main exiting")
}

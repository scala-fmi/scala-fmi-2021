package threads

object ThreadsSharingData extends App {
  // Нишката thread по-долу не вижда промените по тази променлива
  // Ако тя се маркира като @volatila тогава видимостта ще сработи.
  var improveCalculation = true

  val thread = new Thread(() => {
    var i = 0L

    while (improveCalculation) {
      i += 1
    }

    println(s"Thread exiting: $i")
  })

  thread.start()

  println("Main going to sleep...")
  Thread.sleep(1000)
  println("Main waking up...")

  improveCalculation = false

  thread.join()

  println("Main exiting")
}

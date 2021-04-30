package console

import concurrent.io.IO

import scala.concurrent.ExecutionContext
import scala.io.StdIn

class ConsoleForIO(blockingEc: ExecutionContext) {
  def getStringLine: IO[String] = IO(StdIn.readLine()).bindTo(blockingEc)
  def putStringLine(str: String): IO[Unit] = IO(println(str)).bindTo(blockingEc)
}

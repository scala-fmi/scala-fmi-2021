package concurrent

import java.util.concurrent.Executors.newCachedThreadPool
import java.util.concurrent.{Executor, ForkJoinPool}
import scala.concurrent.ExecutionContext

object ExecutionContexts {
  implicit val default: ExecutionContext = ExecutionContext.fromExecutorService(new ForkJoinPool)

  val currentThreadEc = ExecutionContext.fromExecutor {
    (operation: Runnable) => operation.run()
  }

  val blocking = ExecutionContext.fromExecutorService(newCachedThreadPool())
}

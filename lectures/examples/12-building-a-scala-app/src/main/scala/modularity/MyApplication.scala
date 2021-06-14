package modularity

import com.typesafe.config.{Config, ConfigFactory}
import modularity.a.{A1, A2, A3, AModule}
import modularity.b.{B1, B2, BModule}
import modularity.c.{C, CModule}
import modularity.d.DModule

object MyApplication extends CModule with BModule with AModule with DModule  {
  lazy val config: Config = ConfigFactory.load()

  def main(args: Array[String]): Unit = {
    c.doSomething()
    println(d)
  }
}

package modularity.d

import com.typesafe.config.Config

trait D
class D1 extends D
class D2 extends D

trait DModule {
  def config: Config

  lazy val d: D =
    if (config.getString("myApplication.dVersion") == "d1") new D1
    else new D2
}

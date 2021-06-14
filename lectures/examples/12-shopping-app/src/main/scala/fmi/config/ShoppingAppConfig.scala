package fmi.config

import fmi.infrastructure.db.DbConfig
import io.circe.Codec

case class ShoppingAppConfig(
  secretKey: String,
  http: HttpConfig,
  database: DbConfig
)

object ConfigJsonCodecs {
  import io.circe.generic.semiauto._

  implicit val httpConfigCodec: Codec[HttpConfig] = deriveCodec
  implicit val dbConfigCodec: Codec[DbConfig] = deriveCodec
  implicit val shoppingAppConfigCodec: Codec[ShoppingAppConfig] = deriveCodec
}

package fmi.shopping

import cats.effect.IO
import fmi.user.AuthenticatedUser
import io.circe.Codec
import org.http4s.AuthedRoutes
import org.http4s.dsl.io._
import cats.implicits._
import io.circe.generic.extras.semiauto.deriveUnwrappedCodec
import io.circe.generic.semiauto.deriveCodec

class ShippingRouter(orderService: OrderService) {
  import OrderJsonCodecs._
  import org.http4s.circe.CirceEntityCodec._

  def authenticatedRoutes: AuthedRoutes[AuthenticatedUser, IO] = AuthedRoutes.of[AuthenticatedUser, IO] {
    case authReq @ POST -> Root / "orders" as user =>
      val placedOrder = authReq.req.as[ShoppingCart] >>= (orderService.placeOrder(user.id, _))

      placedOrder.flatMap {
        _.fold(_ => Conflict(), Ok(_))
      }
  }
}

object OrderJsonCodecs {
  import fmi.inventory.InventoryJsonCodecs.productSkuCodec
  import fmi.user.UsersJsonCodecs.userIdCodec

  implicit val orderLineCodec: Codec[OrderLine] = deriveCodec
  implicit val shoppingCartCodec: Codec[ShoppingCart] = deriveCodec

  implicit val orderIdCodec: Codec[OrderId] = deriveUnwrappedCodec
  implicit val orderCodec: Codec[Order] = deriveCodec
}

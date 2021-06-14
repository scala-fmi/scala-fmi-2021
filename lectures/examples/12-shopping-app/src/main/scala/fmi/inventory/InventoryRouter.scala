package fmi.inventory
import cats.effect.IO
import cats.implicits._
import fmi.user.{AuthenticatedUser, UserRole}
import io.circe.Codec
import io.circe.generic.extras.semiauto.deriveUnwrappedCodec
import io.circe.generic.semiauto.deriveCodec
import org.http4s.dsl.io._
import org.http4s.{AuthedRoutes, HttpRoutes}

class InventoryRouter(productDao: ProductDao, productStockDao: ProductStockDao) {
  import InventoryJsonCodecs._
  import org.http4s.circe.CirceEntityCodec._

  def nonAuthenticatedRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "stock" =>
      Ok(productStockDao.retrieveAllAvailableStock)

    case GET -> Root / "products" / sku =>
      val productSku = ProductSku(sku)

      productDao.retrieveProduct(productSku).flatMap(
        _.fold(NotFound())(Ok(_))
      )
  }

  def authenticatedRoutes: AuthedRoutes[AuthenticatedUser, IO] = AuthedRoutes.of[AuthenticatedUser, IO] {
    case authReq @ POST -> Root / "products" as user if user.role == UserRole.Admin =>
      Ok(authReq.req.as[Product] >>= productDao.addProduct)

    case authReq @ POST -> Root / "stock" as user if user.role == UserRole.Admin =>
      val adjustmentResult = authReq.req.as[InventoryAdjustment] >>= productStockDao.applyInventoryAdjustment

      adjustmentResult.flatMap {
        case SuccessfulAdjustment => Ok()
        case NotEnoughStockAvailable => Conflict()
    }
  }
}

object InventoryJsonCodecs {
  implicit val productSkuCodec: Codec[ProductSku] = deriveUnwrappedCodec
  implicit val productCodec: Codec[Product] = deriveCodec
  implicit val productStockCodec: Codec[ProductStock] = deriveCodec

  implicit val productStockAdjustmentCodec: Codec[ProductStockAdjustment] = deriveCodec
  implicit val inventoryAdjustmentCodec: Codec[InventoryAdjustment] = deriveCodec
}

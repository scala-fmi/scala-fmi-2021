package http

import org.http4s.{HttpRoutes, ParseFailure, QueryParamDecoder}
import cats.effect.IO
import cats.implicits._
import http.Utils._
import org.http4s.dsl.io._
import org.http4s.implicits._

import java.time.Year
import scala.util.Try

object Example3_QueryParameters extends App {

  object CountryQueryParamMatcher extends QueryParamDecoderMatcher[String]("country")

  implicit val yearQueryParamDecoder: QueryParamDecoder[Year] =
    QueryParamDecoder[Int].map(Year.of)

  object YearQueryParamMatcher extends QueryParamDecoderMatcher[Year]("year")

  def getAverageTemperatureForCountryAndYear(country: String, year: Year): IO[Double] = IO(21.34)

  val averageTemperatureService = HttpRoutes.of[IO] {
    case GET -> Root / "weather" / "temperature" :? CountryQueryParamMatcher(country) +& YearQueryParamMatcher(year)  =>
      Ok(getAverageTemperatureForCountryAndYear(country, year).map(s"Average temperature for $country in $year was: " + _))
    case _ =>
      Ok("Did not match the above")
  }

  println(requestEntityUnsafe[String](averageTemperatureService)(get(uri"/weather/temperature?country=Bulgaria&year=2020")))
  println(requestEntityUnsafe[String](averageTemperatureService)(get(uri"/weather/temperature?country=Bulgaria&year=asdasd")))


  // Optional Query Parameters
  object OptionalYearQueryParamMatcher extends OptionalQueryParamDecoderMatcher[Year]("year")

  def getAverageTemperatureForCurrentYear: IO[String] = IO("15.13")
  def getAverageTemperatureForYear(y: Year): IO[String] = IO("19.08")

  val routes2 = HttpRoutes.of[IO] {
    case GET -> Root / "temperature" :? OptionalYearQueryParamMatcher(maybeYear) =>
      maybeYear match {
        case None =>
          Ok(getAverageTemperatureForCurrentYear)
        case Some(year) =>
          Ok(getAverageTemperatureForYear(year))
      }
  }

  // Validating query parameters
  val validatingYearQueryParamDecoder: QueryParamDecoder[Year] =
    QueryParamDecoder[Int].emap(i => Try(Year.of(i)).toEither.leftMap(t => ParseFailure(t.getMessage, t.getMessage)))

  object ValidatingYearQueryParamMatcher extends ValidatingQueryParamDecoderMatcher[Year]("year")(validatingYearQueryParamDecoder)

  val routes = HttpRoutes.of[IO] {
    case GET -> Root / "temperature" :? ValidatingYearQueryParamMatcher(yearValidated) =>
      yearValidated.fold(
        _ => BadRequest("unable to parse argument year"),
        year => getAverageTemperatureForYear(year).map(s"Average temperature for $year was: " + _).flatMap(Ok(_))
      )
  }


  println(requestEntityUnsafe[String](routes)(get(uri"/temperature?year=2020")))
  println(requestEntityUnsafe[String](routes)(get(uri"/temperature?year=notyear")))

  // Optional Validating Query Parameters
  object LongParamMatcher extends OptionalValidatingQueryParamDecoderMatcher[Long]("long")
}

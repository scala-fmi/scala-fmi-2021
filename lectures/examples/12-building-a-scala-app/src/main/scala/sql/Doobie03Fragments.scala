package sql

import cats.data.NonEmptyList
import doobie._
import doobie.implicits._

object Doobie03Fragments {
  def selectCountries(filter: Fragment): Query0[Country] = {
    sql"""
         SELECT code, name, population, gnp
         FROM country
         WHERE $filter
       """
      .query[Country]
  }

  def populationInRange(range: Range) = {
    selectCountries(fr"${range.min} < population AND population < ${range.max}")
  }

  val ex1 = populationInRange(100000000 to 300000000).to[List]

  def countriesByCodes(codes: NonEmptyList[String]) = {
    selectCountries(Fragments.in(fr"code", codes))
  }

  val ex2 = countriesByCodes(NonEmptyList.of("USA", "BRA", "PAK", "GBR")).to[List]
}

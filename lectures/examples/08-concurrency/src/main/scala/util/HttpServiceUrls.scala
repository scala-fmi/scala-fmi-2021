package util

object HttpServiceUrls {
  def randomNumberUpTo(n: Int) = s"https://www.random.org/integers/?num=1&min=1&max=$n&col=1&base=10&format=plain"
}

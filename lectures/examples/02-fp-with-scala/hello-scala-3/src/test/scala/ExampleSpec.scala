import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ExampleSpec extends AnyFlatSpec with Matchers {
  "+" should "sum two numbers" in {
    2 + 3 should be (5)
  }
}

import cats.data.Validated.{Invalid, Valid}
import effects.Applicative
import validation.FormValidatorNecApplicative.ValidationResult

object Answers {

  val validatedApplicative = new Applicative[ValidationResult] {
    def map2[A, B, C](fa: ValidationResult[A], fb: ValidationResult[B])(f: (A, B) => C): ValidationResult[C] =
      (fa, fb) match {
        case (Valid(a), Valid(b)) => Valid(f(a, b))
        case (Valid(_), Invalid(nec)) => Invalid(nec)
        case (Invalid(nec), Valid(_)) => Invalid(nec)
        case (Invalid(nec1), Invalid(nec2)) => Invalid(nec1 ++ nec2)
      }

    def unit[A](a: => A): ValidationResult[A] = Valid(a)
  }
}

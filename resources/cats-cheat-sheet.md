# Cats Cheat Sheet

[Графика на type class-овете](https://cdn.rawgit.com/tpolecat/cats-infographic/master/cats.svg)

Забележка: много от типовете по-долу са опростени от реалната им репрезентация с цел опростение. Също, използването на `implcit class` е само примерно, повечето синтаксис е имплементиран чрез други средства, които позволяват комбинирането му в един `import`.

Забележка: изброения синтаксис не е пълен. Избрали сме функциите, които смятаме, че биха били най-полезни.

Ако желаете да включите целият синтаксис може да кажете `import cats.implicits._` или `import cats.syntax.all._`

## Data types

### `Nested[F[_], G[_], A]`

Осигурява композиция на вложени функтори или апликативи. Така например лесно и четимо могат да бъдат изпълнявани операции като `map` или `mapN` върху най-вътрешните стойности (от втрешния ефект).

```scala
case class Nested[F[_], G[_], A](value: F[G[A]])
```

Ако `F` и `G` са функтори, то `Nested[F, G, *]` образува функтор. Аналогично, ако `F` и `G` са апликативи, то `Nested[F, G, *]` образува апликатив. `.value` ни позволява да излезем от `Nested` и да се върнем обратно към нормалните вложени ефектни стойности.

Синтаксис при `import cats.syntax.nested._`:

```scala
implicit class NestedOps[F[_], G[_], A](value: F[G[A]]) {
  def nested: Nested[F, G, A]
}
```

Монадите не се композират в общия случай, но са композитни, ако съществува `Traverse` за вътрешния тип. Поради тази причина в Cats имаме няколко типа за комопозиране на `Traverse` монади с други – `OptionT` и `EitherT`.

[Примери с композиция](../lectures/examples/11-cats-and-cats-effects/src/main/scala/cats/Composition.scala)

### `Option[A]`

Синтаксис при `import cats.syntax.option._`:

```scala
def none[A]: Option[A] // None, но разглеждан като Option[A]. Позволява лесна работа в type class-ове върху Option (тъй като те иначе не работят върху конкретните типове Some и None)

implicit class OptionIdOps[A](a: A) {
  def some: Option[A] // Вкарва стойност в Option. Като Some(a), но разглеждано като Option[A]
}

implicit class OptionOps[A](a: Option[A]) {
  // Помощни методи за различни варианти на Validated и Either
  // Nel ще използва NonEmptyList за грешките
  // Nec ще използва NonEmptyChain за грешките
  
  def toInvalid[B](b: => B): Validated[A, B]
  def toInvalidNel[B](b: => B): ValidatedNel[A, B]
  def toInvalidNec[B](b: => B): ValidatedNec[A, B]

  def toValid[B](b: => B): Validated[B, A]
  def toValidNel[B](b: => B): ValidatedNel[B, A]
  def toValidNec[B](b: => B): ValidatedNec[B, A]

  def toRightNel[B](b: => B): EitherNel[B, A]
  def toRightNec[B](b: => B): EitherNec[B, A]
  
  def toLeftNel[B](b: => B): EitherNel[A, B]
  def toLeftNec[B](b: => B): EitherNec[A, B]

  def orEmpty(implicit A: Monoid[A]): A // Взима вложената стойност, или дава идентитета на моноида, ако няма такава

  def toOptionT[F[_]: Applicative]: OptionT[F, A] // Композира option-а с друг ефект
}
```

[Примери](../lectures/examples/11-cats-and-cats-effects/src/main/scala/cats/DataTypesSyntax.scala)

### `Either[E, A]`

Синтаксис при `import cats.syntax.either._`:

```scala
implicit class EitherIdOps[A](a: A) {
  // Преобразуване на нормална стойност към Either, използвайки съответна колекция за грешките (Nel, Nec или никаква)

  def asLeft[B]: Either[A, B]
  def leftNel[B]: Either[NonEmptyList[A], B]
  def leftNec[B]: Either[NonEmptyChain[A], B]

  def asRight[B]: Either[B, A] = Right(obj)
  def rightNel[B]: Either[NonEmptyList[B], A]
  def rightNec[B]: Either[NonEmptyChain[B], A]
}

implicit class EitherOps[E, A](either: Either[E, A]) {
  def toValidated: Validated[A, B] // преобразуване към Validated

  // Различни начини за спряване с грешката
  def orElse[C, BB >: B](fallback: => Either[C, BB]): Either[C, BB]
  def recover[BB >: B](pf: PartialFunction[A, BB]): Either[A, BB]
  def recoverWith[AA >: A, BB >: B](pf: PartialFunction[A, Either[AA, BB]]): Either[AA, BB]
  
  // ...и други
}
```

[Примери](../lectures/examples/11-cats-and-cats-effects/src/main/scala/cats/DataTypesSyntax.scala)

### `Validated[E, A]`

Структура, подобна на Either, но позволяваща независимо апликативно събиране на множество грешки. Изисква се да съществува полугрупа за `E` за да се извърши събирането. Най-често за `E` се използва типа `NonEmptyChain`. 

Синтаксис при `import cats.syntax.validated._`:

```scala
implicit class ValidatedIdOps[A](a: A) {
  // Преобразуване на нормална стойност към Validated, използвайки съответна колекция за грешките (Nel, Nec или никаква)
  
  def valid[B]: Validated[B, A]
  def validNel[B]: ValidatedNel[B, A]
  def validNec[B]: ValidatedNec[B, A]
  
  def invalid[B]: Validated[A, B]
  def invalidNel[B]: ValidatedNel[A, B]
  def invalidNec[B]: ValidatedNec[A, B]
}
```

[Примери](../lectures/examples/11-cats-and-cats-effects/src/main/scala/cats/DataTypesSyntax.scala)

### `FunctionK` или `~>`

Функция, позволяваща прехвърлянето на стойност от един ефект към друг. Използва се например при `Parallel` по-долу.

```scala
trait FunctionK[F[_], G[_]] {
  def apply[A](fa: F[A]): G[A]
}

type ~>[F[_], G[_]] = FunctionK[F, G]
```

## Type class-ове

### Eq

Предоставя мултиверсално сравнение, тоест такова, при което стойности от различни типове не могат да се сравняват още при компилация (за разлика от при стандартния в Scala `==` оператор, който е универсален).

`===` трябва да образува релация на еквивалентност

```scala
trait Eq[A] {
  def eqv(x: A, y: A): Boolean

  def neqv(x: A, y: A): Boolean = !eqv(x, y)
}
```

Синтаксис при `import cats.syntax.eq._`:

```scala
implicit class EqOps[A: Eq](x: A) {
  def ===(rhs: A): Boolean
  def =!=(rhs: A): Boolean
}
```

[Примери](../lectures/examples/11-cats-and-cats-effects/src/main/scala/cats/EqDemo.scala)

### Semigroup и Monoid

Полугрупа предоставя възможност за събиране на елементи, докато моноид добава наличието на неутрален елемент спрямо тази операция. Операцията трябва да е асоциативна.

```scala
trait Semigroup[A] {
  def combine(x: A, y: A): A
}

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}
```

Синтаксис при `import cats.syntax.monoid._`:

```scala
implicit class SemigroupOps[A: Semigroup](x: A) {
  def |+|(y: A): A // събиране на x и y
  def combineN(n: Int): A // събиране на x със себе си n пъти
}

implicit class MonoidOps[A: Monoid](x: A) {
  def isEmpty(implicit eq: Eq[A]): Boolean // проверява дали стойността е идентитета. Изисква Eq за сравнение
}
```

[Примери](../lectures/examples/11-cats-and-cats-effects/src/main/scala/cats/MonoidDemo.scala)

### `Foldable[F[_]]`

Абстракция за структури, които могат да бъдат fold-вани.

```scala
trait Foldable[F[_]] {
  def foldLeft[A, B](fa: F[A], initial: B)(f: (B, A) => B): B
  def foldRight[A, B](fa: F[A], lInitial: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B]
}
```

`Eval` при `foldRight` значи, че операциите ще се извършат lazy, при нужда. Как работят `Eval` и `foldRight` не е важно за курса.

Синтаксис при `import cats.syntax.foldable._`:

```scala
implicit class FoldableOps[F[_], A](fa: F[A]) {
  def foldLeft[B](b: B)(f: (B, A) => B): B
  def foldRight[B](lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B]
  
  def reduceLeftOption(f: (A, A) => A): Option[A]
  def reduceRightOption(f: (A, Eval[A]) => Eval[A]): Eval[Option[A]]
  
  def minimumOption(implicit A: Order[A]): Option[A]
  def maximumOption(implicit A: Order[A]): Option[A]
  
  def combineAll(implicit m: Monoid[A]): A
  def combineAllOption(implicit s: Semigroup[A]): Option[A]
  def foldMap[B](f: A => B)(implicit m: Monoid[B]): B

  def isEmpty: Boolean
  def nonEmpty: Boolean
  def exists(p: A => Boolean): Boolean
  def forall(p: A => Boolean): Boolean
  def size: Long
  
  def existsM[G[_]: Monad](p: A => G[Boolean]): G[Boolean] // като exists, но с резултат, изпълнен в монада
  def forallM[G[_]: Monad](p: A => G[Boolean]): G[Boolean] // като forall, но с резултат, изпълнен в монада
    
  // и други операции, характерни за foldable типове
}
```

[Примери](../lectures/examples/11-cats-and-cats-effects/src/main/scala/cats/FoldableDemo.scala)

### `Functor[F[_]]`

Абстракция за ефекти, чиято стойност може да бъде трансформирана към друга. Аксиоми:

* идентитет: `fa.map(x => x) == fa`
* композиция: `fa.map(f).map(g) == fa.map(f.andThen(g))`

```scala
trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}
```

Синтаксис при `import cats.syntax.functor._`:

```scala
implicit class FunctorOps[F[_]: Functor, A](fa: F[A]) {
  def map[B](f: A => B): F[B] // операцията map за функтора
  
  def as[B](b: B): F[B] // игнорира резултатът във функтора, заменяйки го със стойността b
  def void: F[Unit] // игнорира резултатът във функтора и го заменя със () (т.е със unit)
  def widen[B >: A]: F[B] // разглежда стойността на функтора като неѝн надтип. Полезно за имитиране на ковариантност
}

implicit class FunctorTuple2Ops[F[_]: Functor, A, B](fab: F[(A, B)]) {
  def swapF: F[(B, A)] // разменя A и B
  def unzip: (F[A], F[B]) // изважда tuple-а отвън
}
```

[Примери](../lectures/examples/11-cats-and-cats-effects/src/main/scala/cats/FunctorDemo.scala)

### `Apply[F[_]]`

Абстракция за ефекти, които могат да бъдат комбинирани независимо (и потенциално паралелизуемо). Аксиоми:

* асоциативност: `fa.product(fb).product(fc) = fa.product(fb.product(fc)).map { case (a, (b, c)) => ((a, b), c) }`

```scala
trait Apply[F[_]] extends Functor[F] {
  def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]
  
  def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
    ap(map(fa)(a => (b: B) => (a, b)))(fb)

  def map2[A, B, Z](fa: F[A], fb: F[B])(f: (A, B) => Z): F[Z] =
    map(product(fa, fb))(f.tupled)
}
```

Синтаксис при `import cats.syntax.apply._`

```scala
implicit class ApplyOps[F[_]: Apply, A](fa: F[A]) extends Serializable {
    def <*>[B, C](fb: F[B])(implicit ev: A <:< (B => C)): F[C] // символна репрезентация на ap
    def *>[B](fb: F[B]): F[B] // комбинира fa и fb, но взима резултата само от fb
    def <*[B](fb: F[B]): F[A] // комбинира fa и fb, но взима резултата само от fa
  
    def product[B](fb: F[B]): F[(A, B)] // комбинира fa и fb в tuple (изисква import cats.syntax.semigroupal._)
    def map2[B, C](fb: F[B])(f: (A, B) => C): F[C] // map-ва fa и fb с функцията f
}

implicit class Tuple2ApplyOps[F[_]: Apply, A, B](t2: (F[A], F[B]))  {
  def mapN[Z](f: (A, B) => Z): F[Z] // прилагане на функция към две ефекти стйоности
  def tupled: F[(A, B)] // product върху двата елемента
}

implicit class Tuple3ApplyOps[F[_]: Apply, A, B, C](t3: (F[A], F[B], F[C])) {
  def mapN[Z](f: (A, B, C) => Z): F[Z] // прилагане на функция към три ефекти стйоности
  def tupled: F[(A0, A1, A2)] // product върху трите елемента
}

// ... имплементации за tuple от 1 до 22
```

[Примери](../lectures/examples/11-cats-and-cats-effects/src/main/scala/cats/ApplyApplicativeTraverseDemo.scala)

### `Applicative[F[_]]`

Добавя към `Apply` възможността за вкарване на стойност в ефекта. Допълнителни аксиоми:

* ляв идентитет: `pure(()).product(fa).map(_._2) = fa`
* десен идентитет: `fa.product(pure(())).map(_._1) = fa`

```scala
trait Applicative[F[_]] extends Apply[F] {
  def pure[A](x: A): F[A]

  def map[A, B](fa: F[A])(f: A => B): F[B] =
    ap(pure(f))(fa)
}
```

Синтаксис при `import cats.syntax.applicative._`:

```scala
implicit class ApplicativeIdOps[A](a: A) {
  def pure[F[_]: Applicative]: F[A] // вкарва произволна стойност a във ефекта F
}

implicit class ApplicativeOps[F[_]: Applicative, A](fa: F[A]) {
  def replicateA(n: Int): F[List[A]] // повтаря fa n пъти
}
```

[Примери](../lectures/examples/11-cats-and-cats-effects/src/main/scala/cats/ApplyApplicativeTraverseDemo.scala)

### `Traverse[F[_]]`

Абстракция за структури, които могат да бъдат traverse-вани чрез определен апликатив.

```scala
trait Traverse[F[_]] extends Functor[F] with Foldable[F] {
  def traverse[G[_] : Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]]

  def sequence[G[_]: Applicative, A](fga: F[G[A]]): G[F[A]] = traverse(fga)(ga => ga)

  def map[A, B](fa: F[A])(f: A => B): F[B] = traverse[Id, A, B](fa)(f)
}
```

Забележка: с цел оптимизация `traverse` оставя `foldLeft` и `foldRight` абстрактни. Тяхна имплементация чрез traverse е възможна, но неефективна

Синтаксис при `import cats.syntax.traverse._`:

```scala
implicit class TraverseOps[F[_]: Traverse, A](fa: F[A]) {
  def traverse[G[_]: Applicative, B](f: A => G[B]): G[F[B]] // извиква traverse
  def sequence[G[_]: Applicable, B](implicit ev: A <:< G[B]): G[F[B]] // извиква sequence
  
  def flatTraverse[G[_]: Applicative, B](f: A => G[F[B]])(implicit f: FlatMap[F]): G[F[B]] // traverse със flat-ване на Traverse колекцията
  def flatSequence[G[_]: Applicative, B](implicit ev: A <:< G[F[B]], f: FlatMap[F]): G[F[B]] // sequence със flat-ване
  
  def mapWithIndex[B](f: (A, Int) => B): F[B] // map-ване с функция, приемаща и елемента и индекса
  def zipWithIndex: F[(A, Int)] // преобразуване на стойностите към tuple с индекса
}
```

[Примери](../lectures/examples/11-cats-and-cats-effects/src/main/scala/cats/ApplyApplicativeTraverseDemo.scala)

### `FlatMap[F[_]]`

Абстракция за ефекти, които се композират и изпълняват последователно, едно след друго. `flatMap` определя как да продължи композицията. Аксиоми:

* асоциативност:
  Нека `fa: F[A]` и `f: A => F[B]`, `g: B => F[C]`. Тогава:  
  
  `fa.flatMap(f).flatMap(g) == fa.flatMap(a => f(a).flatMap(g))`

```scala
trait FlatMap[F[_]] extends Apply[F] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}
```

Синтаксис при `import cats.syntax.flatMap._`:

```scala
implicit class FlatMapOps[F[_]: FlatMap, A](fa: F[A]) {
  def flatMap[B](f: A => F[B]): F[B] // изпълнява flatMap операцията
  def flatten[B](implicit ev: A <:< F[B]): F[B] // flatten-ва F[F[A]] до F[A]

  def >>=[B](f: A => F[B]): F[B] // символно име за flatMap
  def >>[B](fb: => F[B]): F[B] // игнорира стойността във fa и продължава изпълнението с fb

  def foreverM: F[Nothing] // изпълнява fa безкрайно
}

implicit class FlatMapBooleanOps[F[_]: FlatMap](fBoolean: F[Boolean]) {
  def ifM[B](ifTrue: => F[B], ifFalse: => F[B]): F[B] // Ако fa се оцени до истина, то изчислението продължава с ifTrue, иначе с ifFalse
}

implicit class FlatMapOptionOps[F[_]: FlatMap, A](fOptA: F[Option[A]]) {
  def untilDefinedM: F[A] // повтаря fOptA докато не получи стойност. Полезно е при polling операции
}
```

[Примери](../lectures/examples/11-cats-and-cats-effects/src/main/scala/cats/FlatMapMonadMonadErrorDemo.scala)

### `Monad[F[_]]`

Добавя към `FlatMap` възможността за вкарване на стойност в ефекта. Допълнителни аксиоми:

* ляв идентитет: `pure(a).flatMap(f) == f(a)`
* десен идентитет: `fa.flatMap(unit) == fa`

```scala
trait Monad[F[_]] extends FlatMap[F] with Applicative[F]
```

Синтакс при `import cats.syntax.monad._`

```scala
implicit class MonadOps[F[_]: Monad, A](fa: F[A]) {
  def iterateWhile(p: A => Boolean): F[A] // повтаря fa докато p дава истина за стойността във fa
  def iterateUntil(p: A => Boolean): F[A] // повтаря fa докато p не даде истина за стойността във fa
}
```

[Примери](../lectures/examples/11-cats-and-cats-effects/src/main/scala/cats/FlatMapMonadMonadErrorDemo.scala)

### `ApplicativeError[F[_], E]`

Добавя възможност за трансформиране на апликативни ефекти, съдържащи грешки `E`. Често за `E` се използва `Throwable`.

Някои свойства:

* `raiseError[A](e).handleErrorWith(f) === f(e)`
* `a.pure[G].handleErrorWith(f) === a.pure[G]` (т.е. вкарването на стойност в ефекти никога не води до грешка)

```scala
trait ApplicativeError[F[_], E] extends Applicative[F] {
  def raiseError[A](e: E): F[A]
  def handleErrorWith[A](fa: F[A])(f: E => F[A]): F[A]
}
```

Синтаксис при `import cats.syntax.applicativeError._`

```scala
implicit class ApplicativeErrorIdOps[E](e: E) {
  def raiseError[F[_], A](implicit F: ApplicativeError[F, _ >: E]): F[A] // вкарва произволна грешка в ефект
}

implicit class ApplicativeErrorOps[F[_], E, A](fa: F[A]) {
  def handleError(f: E => A)(implicit F: ApplicativeError[F, E]): F[A] // трансформиране на грешка към успешен резултат

  def handleErrorWith(f: E => F[A])(implicit F: ApplicativeError[F, E]): F[A] // трансформиране на грешка към последващ ефект (още наричано flatMapError)

  def attempt(implicit F: ApplicativeError[F, E]): F[Either[E, A]] // трансформиране към Either с грешката или с успешната стойност
  
  def recover(pf: PartialFunction[E, A])(implicit F: ApplicativeError[F, E]): F[A] // трансформиране на познати грешки към успешен резултат

  def recoverWith(pf: PartialFunction[E, F[A]])(implicit F: ApplicativeError[F, E]): F[A] // трансфомиране на позната грешка към последващ ефект

  def redeem[B](recover: E => B, f: A => B)(implicit F: ApplicativeError[F, E]): F[B] // при грешка я трансформира до успех чрез recover, а при успех той бива трансформиран чрез f

  def onError(pf: PartialFunction[E, F[Unit]])(implicit F: ApplicativeError[F, E]): F[A] // като recoverWith, но със side effecting операция

  def orElse(other: => F[A])(implicit F: ApplicativeError[F, E]): F[A] // при грешка, игнорира грешката и продължава с other
  
  def adaptErr(pf: PartialFunction[E, E])(implicit F: ApplicativeError[F, E]): F[A] // замяна на грешката с друга

  def orRaise(other: => E)(implicit F: ApplicativeError[F, E]): F[A] // при грешка заменя грешката с other
}
```

[Примери](../lectures/examples/11-cats-and-cats-effects/src/main/scala/cats/FlatMapMonadMonadErrorDemo.scala)

### `MonadError[F[_], E]`

Добавя възможност за трансформиране на монадни ефекти, съдържащи грешки `E`. Често за `E` се използва `Throwable`.

Допълнителни свойства:

* `raiseError[A](e).flatMap(f) === F.raiseError[A](e)` (т.е. грешките прекъсват композицията)

```scala
trait MonadError[F[_], E] extends ApplicativeError[F, E] with Monad[F]

type MonadThrow[F[_]] = MonadError[F, Throwable]
```

Синтаксис при `import cats.syntax.monadError._`

```scala
implicit class MonadErrorOps[F[_], E, A](fa: F[A]) {
  def reject(pf: PartialFunction[A, E])(implicit F: MonadError[F, E]): F[A] // трансформира на определени успешни стойности до грешка

  def adaptError(pf: PartialFunction[E, E])(implicit F: MonadError[F, E]): F[A] // замяна на грешката с друга

  def redeemWith[B](recover: E => F[B], bind: A => F[B])(implicit F: MonadError[F, E]): F[B] // определя продължение при грешка (recover) и при успешен резултат (bind) (още познато като transformWith)

  def ensure(error: => E)(predicate: A => Boolean)(implicit F: MonadError[F, E]): F[A] // Проверява дали стойността във fa изпълнява определено условив. Ако не – генерира error като грешка в ефекта F

  def ensureOr(error: A => E)(predicate: A => Boolean)(implicit F: MonadError[F, E]): F[A] // Проверява дали стойността във fa изпълнява определено условив. Ако не, то я трансформира към грешка в ефекта F
}
```

[Примери](../lectures/examples/11-cats-and-cats-effects/src/main/scala/cats/FlatMapMonadMonadErrorDemo.scala)

### `Parallel`

Дава възможност за боравене с определен тип `M` както монадно (ефектите се изпълняват последователно, един след друг), така и апликативно (ефектите могат да се изпълнят независимо и евентуално паралелизуемо).

Апликативните трансформации изискват временно преобразуване на типа към апликативен такъв `F` (което се извършва чрез операцията `parallel`). След извършване на операциита типа се преобразува обратно към монадния (чрез `sequential`)

Типични примери са `Either` за `M` и `Validated` за `F`; `IO` за `M` и `ParIO` за `F`; `List` за `M` и `ZipList` за `F`

```scala
trait Parallel[M[_]] {
  type F[_]
  
  def applicative: Applicative[F]
  def monad: Monad[M]

  def sequential: F ~> M
  def parallel: M ~> F
}
```

(съществува и вариант на `Parallel` с името `NonEmptyParallel`, който е базиран върху `FlatMap` и `Apply`)

Синтаксис при `import cats.syntax.parallel._`

```scala
implicit class ParallelOps[T[_]: Traverse, A](ta: T[A]) {
  def parTraverse[F[_]: Parallel, B](f: A => F[B]): F[T[B]] // изпълнява traverse според апликатива
  def parSequence[F[_]: Parallel]: M[T[A]] // изпълнява sequence според апликатива
}

implicit class Tuple2ParallelOps[F[_]: Parallel, A, B](t2: (F[A], F[B]))  {
  def parMapN[Z](f: (A, B) => Z): F[Z] // прилагане на функция към две ефекти стйоности, използвайки апликатив
  def parTupled: F[(A, B)] // product върху двата елемента, използвайки апликатив
}

implicit class Tuple3ParallelyOps[F[_]: Parallel, A, B, C](t3: (F[A], F[B], F[C])) {
  def parMapN[Z](f: (A, B, C) => Z): F[Z] // прилагане на функция към три ефекти стйоности, използвайки апликатив
  def parTupled: F[(A0, A1, A2)] // product върху трите елемента, използвайки апликатив
}

// ... имплементации за tuple от 1 до 22
```

[Примери 1](../lectures/examples/11-cats-and-cats-effects/src/main/scala/cats/ConcurrentParallelDemo.scala)

[Примери 2](../lectures/examples/11-cats-and-cats-effects/src/main/scala/cats/ParallelDemo.scala)

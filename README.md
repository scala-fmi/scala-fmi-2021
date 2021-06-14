# Функционално програмиране за напреднали със Scala

## Лекции

* [01 – За курса](https://scala-fmi.github.io/scala-fmi-2021/lectures/01-intro.html)
* [02 – Въведение във функционалното програмиране със Scala](https://scala-fmi.github.io/scala-fmi-2021/lectures/02-fp-with-scala.html) \[[код](lectures/examples/02-fp-with-scala)\]
  - Scala инструменти
    * `scala`, `scalac`, REPL
    * sbt, конфигурация, компилиране, стартиране и тестване
    * добавяне на библиотеки
  - Типове и литерали
  - Дефиниции – `val`, `var`, `def`, `type`, функции. Type inference
  - Файлове и пакети
  - Стойностите като обекти (с методи)
  - `if` контролна структура
  - "Всичко е израз или дефиниция". Контролните структури и блоквете като изрази
  - Типова йеархия. `Any`, `AnyVal`, `AnyRef`, `Unit`, `Null`, `Nothing`
  - Java Memory Model
  - Контролни структури със странични ефекти – `while`, `try/catch` (и изключения), `for`
  - Още контролни структури без странични ефекти – pattern matching, (функционален) `for`
  - Съставни типове
    * хетерогенни – наредени n–торки (tuples)
    * хомогенни – `Range`, `List[A]`, `Set[A]`, `Map[K, V]`
  - Операции над хомогенни колекции – `isEmpty`, `head`, `tail`, `take`, `drop`
  - Помощен синтаксис при функции
    * типови параметри (generics)
    * overloading
    * default стйности на параметри
    * именувани аргументи
    * променлив брой параметри
  - Функционално програмиране
    * математически функции
    * функционално срещу императивно програмиране
    * характеристики на функционалните програми
    * Substitution модел на изчисление. Предаване на параметрите по стойност и по име
    * Референтна прозрачност
* [03 – ООП във функционален език](https://scala-fmi.github.io/scala-fmi-2021/lectures/03-oop-in-a-functional-language.html) \[[код](lectures/examples/03-oop-in-a-functional-language)\]
  - Обектно-ориентирано програмиране като система от обекти
    * Съобщения, енкапсулация и late-binding
    * ООП като концепция, ортогонална на ФП. ООП в Scala
  - Дефиниране на клас. Параметри на клас (конструктори), членове, модификатори на достъп
  - Дефиниране на обект
  - `apply` метод. Обекти, приложими като функции
  - Придружаващи обекти
  - implicit конверсии
  - `case` класове за дефиниране на неизминими value обекти
  - Абстрактни типове – trait 
    * Uniform Access Principle
    * множествено наследяване
    * подтипов полиморфизъм
  - `import` и `export` клаузи
  - Обвиващи `AnyVal` класове. Type safety чрез обвиване на обикновени типове
  - Съвместимост на типове. Номинално и Структурно типизиране
  - Структурно типизиране в Scala
  - Типова алгебра. Обединение и сечение на типове
  - The Expression Problem в Scala. ООП срещу ФП конструкции
  - Extension методи – ретроактивно добавяне на методи към тип
  - Тестове
* [04 – Основни подходи при функционалното програмиране](https://scala-fmi.github.io/scala-fmi-2021/lectures/04-key-fp-approaches.html) \[[код](lectures/examples/04-key-fp-approaches)\]
  - Рекурсия
    * итерация чрез рекурсия
    * рекурсия и substitution модела
    * опашкова рекурсия. Рекурсия чрез акумулатори
    * рекурсия по колекции
  - Неизменимост и неизменими структури от данни
    * неизменими обекти във времето
    * persistence (персистентност) и structural sharing. Роля на Garbage Collector-а
    * персистентност и structural sharing при списък (`List`)
    * `Vector` – оптимизация за произволен достъп и добавяне на елементи в началото и в края. Ефективно константна сложност
    * чисто функционални структури от данни. Безопасно споделяне на такива структури между компоненти и нишки
    * `Set` и `Map` като чисто функционални структури
  - Функциите като първокласни обекти
    * ламбда синтаксис и функционален тип
    * преобразуване на `def` функции/методи към обектни функции – eta expansion
    * частично прилагане на аргументи
    * функции от по-висок ред. `filter`, `map`, `reduce`, `fold`
    * композиция на функции от по-висок ред
    * множество списъци с параметри. 
      - групиране на параметри 
      - type inference 
      - създаване на собствени конструкции 
      - currying
    * Операции с функции. Композиция, преобразуване към `curried` или `tupled` форми
  - Композиция на функции
  - Изразяване чрез типове 
* [05 – Fold и колекции](lectures/05-folds-collections.ipynb)
  - Абстракция чрез `fold`, `foldLeft` и `foldRight`. Изразяване на операции чрез тях
  - Йеархия и дизайн на колекциите в Scala.
    * Uniform Return Type principle
    * Колекциите като функции (примери за `Seq`, `Set`, `Map`)
  - Честични функции. Частични функции чрез `case` блокове
  - Още операции върху колекции – `partition`, `span`, `splitAt`; `flatten`, `flatMap`, `zip`, `groupBy` и други
  - Тайната за `for` конструкциите – синтактична захар върху `map`, `flatMap` и `withFilter` (`filter`)
  - Lazy колекции
    * нестриктни view-та на колекции
    * lazy списъци с мемоизация
  - [Имплементация на `LazyList`](lectures/examples/05-folds-collections/lazy-list)
* [06 – Pattern matching и алгебрични типове от данни (ADTs)](lectures/06-adts-are-the-root-of-all-evil.ipynb)
  - Pattern matching. Деструктуриране на обекти
  - Pattern matching на различни места
    * `case` блокове – при `match` или на мястата, където се очакват функции или частични функции. Примери с `map` и `collect`
    * pattern bindings – съпоставяне с единичен pattern при `for` (отляво на генератори и дефиници) и при `val` дефиниции
  - Алгебрични типове от данни. Примери
    * product типове. `case` класове, наредени n-торки и други
    * sum типове. Имплементация чрез `sealed trait` и `enum`
  - Type bounds (ограничения върху типовите параметри). 
  - Вариантност. Ковариантност и контравариантност на типове. Вариантност при типовете за функции. [Още примери](https://www.freecodecamp.org/news/understand-scala-variances-building-restaurants/)
  - Изразяване на опционалност. `null` като грешка за милиард долара. Имплементация на `Option` тип
  - Полезни операции върху `Option`. [Използване на операции вместо pattern matching](lectures/07-effects-and-functional-error-handling.ipynb)
  - Деструктуриране чрез екстрактори. Използването им в pattern matching
    [тук](lectures/07-effects-and-functional-error-handling.ipynb)
* [07 – Ефекти и функционална обработка на грешки](lectures/07-effects-and-functional-error-handling.ipynb)
  - Функционален дизайн – премахване на нелегалните състояния
  - От частични към тотални функции
  - Ефекти
  - Видове ефекти – частичност, изключения/грешки, недетерминизъм, зависимости/конфигурация, логване, изменяемо състояние, вход/изход, асинхронност, и други. Типове зад тях
  - Проблемите при изключенията
  - Моделиране на грешни състояние чрез `Option`, `Try` и `Either`
    * замяна на изключенията с безопасни и композитни ефекти
    * моделиране на грешките като домейн структури и домейн логика
  - Ефект за вход/изход. IO. Предимства на IO
  - Разделение на чисто функционално композитно изграждане на план (без странични ефекти) от изпълнение на плана (водещо до странични ефекти)
* [08 – Конкурентност](https://scala-fmi.github.io/scala-fmi-2021/lectures/08-concurrency.html) \[[код](lectures/examples/08-concurrency)\]
  - Трансформиращи, интерактивни и реактивни системи
  - Характеристики на физическия свят 
  - Цел: моделиране на програми, които да са част от физическия свят и да взаимодействат с него
  - Конкурентност и паралелилизъм – припрокритие и разлики на термините
  - Дистрибутираност
  - Примери за конкурентни модели
  - Какво прави един модел подходящ? Примерни характеристики
  - Нужда от комуникация между конкурентни примитиви
  - Нишки, комуникация между нишки (чрез споделено състояние), happens-before релация върху JVM, проблеми на нишките
  - Реактивност и асинхронност чрез callbacks. Преизползване на нишки чрез thread pool-ове. Проблеми на callbacks
  - Future – ефект за асинхронност. Моделиране на Future, следвайки ползите и принципите на IO
  - Прехвърляне на свойствата на функционалните изрази върху ефекти. `map`, `flatMap`, `zip` и `zipMap` като средство за описване на зависимости върху ефекти вместо върху сурови стойности
  - Eager и lazy Future-и
  - Дизайн и реализация на eager Future. Връзка към външния – чрез Promise. Примерно използване
  - Композитност, реактивност и безопасност на Future
  - Асинхронни HTTP client и HTTP server чрез Future
  - Конзолен вход/изход със Future – използване на отделен thread pool за блокиращи операции
  - Пример – web server за библиотека от книги със web клиент, позволяващ разглеждане на каталога от книги
  - Помощни операции върху Future 
    * трансформация на множество Future-и (`sequence`, `firstCompletedOf`)
    * и на грешки (`recover`, `recoverWith`, `fallbackTo` и други)
  - Lazy Future – ще го кръстим lazy IO
    * дизайн на интерфейса според нужните операции
    * описване на план чрез ADT за IO
    * асинхронен интерпретатор на IO
    * синхронен интерпретатор на IO
  - Предимства на lazy IO през eager Future
* [09 – Type classes](https://scala-fmi.github.io/scala-fmi-2021/lectures/09-type-classes.html) \[[код](lectures/examples/09-type-classes)\]
  - Абстрактност като помощно средство в програмирането. Абстрактност в математиката
  - Какво е type class. Пример чрез моноид
  - Контекст в програмния код. Контекстност на type class-овете в Scala
  - Характеристики на type class-овете. Операции, композитност, аксиоми
  - Context bound
  - Логически изводи при търсене на implicit стойности, логическа типова система
  - Защо спазването на аксиомите е важно. Пример с паралелен `fold`
  - [Реализация на type class-ове в Scala 3 чрез `given`](lectures/examples/09-type-classes-scala-3)
  - Сравнение между ООП класовете и type class-овете
  - Полиморфизъм и видове полиморфизъм. Static и late binding. Ретроактивност
  - Примери за type class-ове в стандартната библитека на Scala – `Numeric`, `Ordering`
  - Примери в други езици – [Haskell](lectures/examples/09-type-classes/other/monoid.hs) и [Rust](lectures/examples/09-type-classes/other/monoid.rs) (и [още Rust](lectures/examples/09-type-classes/other/shapes.rs)). Сравнение на тях и Scala
  - Библиотеки за type class-ове. Теория на категории
  - Type class-ове за форматиране и сериализация, пример за JSON сериализация
  - `Monoid` и `Semigroup` в библиотеката Cats
  - Допълнителни примери (непокрити на лекциите)
    * Multiversal срещу universal equality, [`Eq` в Cats](lectures/examples/09-type-classes/src/main/scala/cats/EqDemo.scala)
    * Пример за type class-ове на повече типове, [линейно пространство в Spire](lectures/examples/09-type-classes/src/main/scala/spire/VectorSpaceDemo.scala)
* [10 – Монади и апликативи](https://scala-fmi.github.io/scala-fmi-2021/lectures/10-monads-and-applicatives.html) \[[код](lectures/examples/10-monads-and-applicatives)\]
  - Ефекти. Абстракция над общите операции на ефекти
  - Композиране на функции. Аксиоми
  - Ефектни функции
  - Монада – type class за композиране на ефектни функции
  - Дефиниция на монада чрез compose и unit. Аксиоми
  - Дефиниция чрез flatMap и unit. Аксиоми
  - Производни операции на монади. `map`, `flatten`, `zip`, `map2`, `sequence` и други. Алтернативни дефиници
  - Реализация на type class за монада, с помощни операции
  - Монадни инстанции на основни ефектни типове
  - Реализация на Maybe (алтернативно име на Option), монадна инстанция към Maybe и пример за работа с получените монадни операции
  - `Id` монада
  - [State монада](lectures/examples/10-monads-and-applicatives/src/main/scala/effects/state/state-monad.md)
  - Функтори като генерализация на монадите. Аксиоми
  - Грешни състояния на монади
  - [Пример](lectures/examples/10-monads-and-applicatives/src/main/scala/validation/FormValidatorNec.scala) за валидация с натрупване на грешките с използване на монада за `Validated`  
  - Апликатив. Разликите между монада и апликатив.
  - [Пример](lectures/examples/10-monads-and-applicatives/src/main/scala/validation/FormValidatorNecApplicative.scala) за валидация с натрупване на грешки с апликатив
  - Производни операции на апликативи. `map3`, `map4`, `sequence`, `traverse`.
  - Апликативни инстанции на основни ефектни типове
  - `Traversable` - генерализация на `sequence` и `traverse`
* [11 – Cats и Cats Effect](https://scala-fmi.github.io/scala-fmi-2021/lectures/11-cats-and-cats-effect.html) \[[код](lectures/examples/11-cats-and-cats-effects)\]
  - Typelevel.scala екосистемата
  - Cats – библиотека, осигуряваща абстракции за функционално програмиране
    * type class-ове
    * техни инстанции
    * синтаксис
    * типове от данни
    * тестване на аксиоми
  - Типове от данни – `Chain`, `Validated`, `Id`, `State`, `FunctionK` (или още `~>`), `Nested`
  - Синтаксис за `Option`, `Either`, и `Validated`
  - [Йеархия от type class-ове](https://cdn.rawgit.com/tpolecat/cats-infographic/master/cats.svg)
  - Type class-ове за сравнение и наредба (`Eq`, `Hash`, `PartialOrder`, `Order`). Синтакси
  - Алгебрични type class-ове. `Semigroup` и `Monoid`. Синтаксис
  - Тестване на аксиоми – пример с моноид за рационални числа
  - Type class-ове за ефекти. Операции и синтаксис към всеки от тях
    * `Foldable`
    * `Functor`
    * `Apply`, `Applicative` и `Traverse`
    * `ApplicativeError`
    * `FlatMap`, `Monad` и `MonadError`
  - `Parallel` – type class за типове, позволяващи последователна (монадна) и паралелна (апликативна) обработка
    * примери с инстанции за `IO`/`IO.Par`, `Either`/`Validated` и `List`/`ZipList`
    * синтаксис за `Parallel`
  - Композитност на функтор, апликатив и монада
    * композиране на функтор и апликатив. Тип `Nested`
    * невъзможност за композиция на монади в общия случай. Композитност при вложени ефекти, които имат `sequence` (тоест са `Traverse`). Типове `OptionT` и `EitherT` като решение за тези два конкретни типа
  - [Всички примери за Cats](lectures/examples/11-cats-and-cats-effects/src/main/scala/cats)
  - Cats Effect и type class-ове за конкурентност. Имплементацията им в типа `IO`
  - Cats Effect fibers. Thread pool-ове. Реализация на приложение чрез `IO`. `IOApp`
  - Feature-и на Cats Effect според type class-овете. [Йеархия на type class-овете](https://typelevel.org/cats-effect/docs/typeclasses)
    * [`MonadCancel`](https://typelevel.org/cats-effect/docs/typeclasses/monadcancel) – добавя възможност за канселиране
    * [`Spawn`](https://typelevel.org/cats-effect/docs/typeclasses/spawn) – изпълнение на множество конкурентни fiber-а
    * [`Concurrent`](https://typelevel.org/cats-effect/docs/typeclasses/concurrent) – възможност за описване на безопасен достъп до споделени ресурси (т. нар. Ref) и за изчакване на ресурси (т. нар. Deferred)
    * [`Clock`](https://typelevel.org/cats-effect/docs/typeclasses/clock) – описване на достъп до текущото време
    * [`Temporal`](https://typelevel.org/cats-effect/docs/typeclasses/temporal) – приспиване на fiber за определено време
    * [`Unique`](https://typelevel.org/cats-effect/docs/typeclasses/unique) – генериране на уникални тоукъни
    * [`Sync`](https://typelevel.org/cats-effect/docs/typeclasses/sync) – адаптиране на синхронни изчисления (блокиращи и неблокиращи) към ефектни (и асинхронни) такива
    * [`Async`](https://typelevel.org/cats-effect/docs/typeclasses/async) – адаптиране на callback-базиран асинхронен API към ефектен такъв
    * Пример за функционално описване на план чрез всеки от type class-овете. Изпълнение на плана – изпълнение на страничните ефекти (чрез `unsafeRun*` методите).
  - Безопасен достъп до ресурси, изискващи затваряне. `Resource` ефект
  - [Всички примери за Cats Effect](lectures/examples/11-cats-and-cats-effects/src/main/scala/io)
* [12 – Изграждане на Scala приложение](https://scala-fmi.github.io/scala-fmi-2021/lectures/12-building-a-scala-app.html) \[[код](lectures/examples/12-building-a-scala-app)\]
  - Какво постигнахме досега? Как можем да използваме ФП при моделиране на конкретни домейни?
  - Избор на IO ефект
  - Структуриране на кода
    * зависимости и dependency injection. Реализация чрез параметри (на функции или конструктор на клас). Ползи
    * навързване на зависимостите. Thin Cake Pattern
    * използване на `Resource` за безопасни инициализация и затваряне на глобални и локални ресурси
  - Kак да разделим програмата ни на модули? Разделение по домейн вместо по слоеве. Ползи
  - Връзка със SQL база от данни чрез Doobie
    * `ConnectionIO` ефект и пробразуване до `IO.` Транзакции
    * [примери с Doobie](lectures/examples/12-building-a-scala-app/src/main/scala/sql).
      - заявки
      - сериализация на данните към и от заявка
      - фрагменти
      - Въвеждане и обновяване на данни – единично и на куп
  - JSON сериализация и десериализация чрез Circe
    * Encoder-и и decoder-и
    * Полуавтоматична деривация encoder-и и decoder-и чрез макроси
    * [Примери със Circe](lectures/examples/12-building-a-scala-app/src/main/scala/json)
  - HTTP приложения чрез http4s
    * HTTP като ефектна функция от `Request` към `Response`
    * HTTP server чрез http4s. Стартиране като `IOApp`
    * HTTP routes – pattern matching по `Request`-а
    * Сериализация и десериализация на entity-та. Връщане и четене на JSON чрез Circe
    * Добавяне на обща функционалност чрез middlewares
    * HTTP клиент
    * [Примери с http4s](lectures/examples/12-building-a-scala-app/src/main/scala/http)
  - Функционални конкурентни потоци чрез Fs2
    * От `LazyList` до ефектен `Stream`
    * Функционални трансфомрации на потоци
    * Връзка с вход/изход – файловата система, TCP, UDP, ...
    * Поточно генериране на резултат от HTTP резултат. Поток от Doobie през http4s сървър до клиент
    * Web socket-и чрез Fs2 и http4s
    * [Примери с Fs2](lectures/examples/12-building-a-scala-app/src/main/scala/streams)
  - [Примерно приложение със сървър и клиент за библиотека](lectures/examples/12-library-app)
  - [Цялостно примерно приложение за уеб магазин](lectures/examples/12-shopping-app)
  
## Допълнителни ресурси

* [Таблица на елементите, съставящи типовата система на Scala](resources/type-elements-in-scala.md)
* [Конкурентност със Akka](https://scala-fmi.github.io/scala-fmi-2021/lectures/xx-concurrency-with-akka.html) \[[код](lectures/examples/xx-akka-examples)\]

## Генериране на лекции

### Setup

Имате нужда от инсталиран [pandoc](https://pandoc.org/installing.html).

Проектът има submodule зависимост към reveal.js. При/след клониране на репото инициализирайте модулите:

    git submodule update –init

**Local with Reaveal + Markdown**

To be able to successfully load .md files w/ the Markdown plugin and no co policy pass the `–allow-file-access-from-files` flag for google chrome. If you use a real browser and not google chrome
you are clearly an adult and can figure out how to fix it for yourself.

### Генериране на конретна лекция

    cd lectures
    ./generate-presentation.sh <лекция>

### Генериране на всички лекции

    cd lectures
    ./build.sh

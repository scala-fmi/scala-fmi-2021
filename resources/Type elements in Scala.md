**Таблица на типовите елементи в Scala**



|           Име           | Синтаксис |                            Пример                            |                           Бележки                            |
| :---------------------: | :-------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
|      ***Типове***       |     -     |                              -                               |                              -                               |
|      Номинален тип      | `String`  |                     `"Viktor"`, `"hi!"`                      |                                                              |
|   Параметризиран тип    | `List[T]` |               `List[Int]`, `Map[Int, String]`                |                                                              |
|   ***Полиморфизъм***    |     -     |                              -                               |                              -                               |
|  Function Overloading   |           | <code>def twice(n: Int) = n * 2<br/>def twice(d: Double) = d * 2<br/>def twice(str: String) = str * 2<br/><br/>twice(10) // 20<br/>twice(10.0) // 20.0<br/>twice("10") // "1010" </code> | Функциите имат еднакво име, но се различават по броя, наредбата или типа на параметрите. |
| Parametric Polymorphism |           |      <code>def repeat[A](value: A, times: Int): List[A] <br> repeat("Hello", 3) // List("Hello", "Hello", "Hello") <br> repeat(1, 2) // List(1, 1) <code>  |                                                              |
|                         |           |                                                              |                                                              |
|                         |           |                                                              |                                                              |



```scala
def twice(n: Int) = n * 2
def twice(d: Double) = d * 2
def twice(str: String) = str * 2

twice(10) // 20
twice(10.0) // 20.0
twice("10") // "1010"
```

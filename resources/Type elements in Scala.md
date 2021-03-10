**Таблица на типовите елементи в Scala**

<table>
	<thead>
		<tr>
			<th>Име</th>
			<th>Синтаксис</th>
			<th>Пример</th>
			<th>Бележки</th>
		</tr>
   		</thead>		
	<tbody>
		<tr>
			<th colspan="4" align="left"><i>Типове</i></th>
		</tr>
	</tbody>
	<tbody>
		<tr>
			<th>Номинален тип</th>
			<td><code>String</code></td>
			<td><code>"Viktor"</code>, <code>"Hi!"</code></td>
			<td></td>
		</tr>
		<tr>
			<th>Параметризиран тип</th>
			<td><code>List[T]</code></td>
			<td><code>List[Int]</code>, <code>Map[Int, String]</code></td>
			<td></td>
		</tr>
	</tbody>
    <tbody>
		<tr>
			<th colspan="4" align="left"><i>Полиморфизъм</i></th>
		</tr>
	</tbody>
    <tbody>
		<tr>
			<th>Overloading</th>
			<td></td>
			<td>
                <pre class="sourceCode scala">
def twice(n: Int) = n * 2
def twice(d: Double) = d * 2
def twice(str: String) = str * 2<br/>
twice(10) // 20
twice(10.0) // 20.0
twice("10") // "1010"</pre>
            </td>
			<td>Функциите имат еднакво име, но се различават по броя, наредбата или типа на параметрите.</td>
		</tr>
		<tr>
			<th>Полиморфизъм чрез типови параметри</th>
			<td></td>
			<td>
<pre>
def repeat[A](value: A, times: Int): List[A]<br/>
repeat("Hello", 3) // List("Hello", "Hello", "Hello")
</pre>
            </td>
			<td></td>
		</tr>
	</tbody>
</table>

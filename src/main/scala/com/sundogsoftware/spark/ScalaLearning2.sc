// Flow control

// if else
if (1 > 3) {
  println("Impossible")
  println("Really?")
} else {
  println("The world makes sense")
  println("still")
}

// matching
val number: Int = 2
number match {
  case 1 => println("One")
  case 2 => println("Two")
  case 3 => println("Three")
  case _ => println("Something else") // similar to default
}

for (x <- 1 to 4) {
  val squared = x * x
  println(squared)
}

var x: Int = 10
while (x >= 5) {
  println(x)
  x -= 1
}

x = 0
do {
  println(x)
  x += 1
} while (x <= 10)

// expression
{val x = 10; x +20}
println({val x =10; x+20})
var a = 0
var b = 1
println(a)
println(b)
for (i <- 2 to 10){
  var c = a + b
  println(c)
  a = b
  b = c
}
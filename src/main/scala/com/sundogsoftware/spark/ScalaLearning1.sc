// VALUES are immutable constants
val hello: String = "Hola!"

// VARIABLES are mutable
var helloThere: String = hello
helloThere = hello + " there!"
println(helloThere)

var immutableHelloThere = hello + " there"
println(immutableHelloThere)

// Data types
val numberOne: Int = 1
val truth: Boolean = true
val letterA: Char = 'a'
val pi: Double = 3.14159465
val piSinglePrecision: Float = 3.14159f
val bigNumber: Long = 12345689
val smallNumber: Byte = 127

// concatenate
println("Here is a mess: " + numberOne +  truth + letterA + pi + bigNumber)
println(f"Pi is about $piSinglePrecision%.3f")
println(f"Zero padding on the left $numberOne%05d")
println(s"I can use the s prefix to use variable like $numberOne $truth $letterA")
println(s"The s prefix isn't limited to variables, I can include any expression. Like ${1+2}")

// regular express
val theUltimateAnswer: String = "To life, the universe, and everything is 42."
val pattern = """.* (\d+).*""".r
val pattern(answerString) = theUltimateAnswer
val answer = answerString.toInt
println(answer)

// Booleans
val isGreater = 1 > 2
val isLesser = 1 < 2
val impossible = isGreater & isLesser
val bits = 5 & 7
val anotherWay = isGreater && isLesser
val picard: String = "Picard"
val bestCaptain: String = "Picard"
val isBest: Boolean = picard == bestCaptain

val doubledPi = pi * 2
println(f"doubled pi = $doubledPi%.3f")
// Data structure

// Tuples
// Immutable lists
val captainStuff = ("Picard", "Enterprise-D", "NCC-1701-D", 36)
println(captainStuff)

// refer to the individual fields with a ONE-BASED index
println(captainStuff._1)
println(captainStuff._2)
println(captainStuff._4)
//println(captainStuff._5) // error

val picardShip = "Picard" -> "Enterprise-D"
println(picardShip._1)
println(picardShip._2)

val aBunchOfStuff = ("Kirk", 1964, true)
println(aBunchOfStuff)

// Lists
// like a tuple, but more functionality
// must be of same type
var ship = List("Enterprise", "Defiant", "Voyager", "Deep Sea")
println(ship.head) // or ship(0), list is zero-based
println(ship(1))
println(ship.tail)

for (s <- ship) {
  println(s)
}

// map
val backwardShips = ship.map((s: String) => {s.reverse})
for (s <- backwardShips) {println(s)}

// reduce() to combine together all items in a collection using some func
val numberList = List(1,2,3,4,5)
var sum = numberList.reduce((x: Int, y:Int) => x + y)
println(sum)

// filter() removes stuff
val iHateFives = numberList.filter((x: Int) => x != 5)
println(iHateFives)

val iHateThrees = numberList.filter(_ != 3) // using placeholder
println(iHateThrees)

// concatenate lists
val moreNumbers = List(6,7,8,9, 10)
val lostOfNumbers = numberList ++ moreNumbers

val reversedNumbers = lostOfNumbers.reverse
val sortedNumbers = reversedNumbers.sorted

val lostOfDuplicates = numberList ++ numberList
println(lostOfDuplicates)
val distinctNumbers = lostOfDuplicates.distinct

val total = distinctNumbers.sum
val hasThree = iHateThrees.contains(3)

// MAPS
val shipMap = Map("Kirk" -> "Enterprise", "Picard" -> "Enterprise-D", "Sisko" -> "Deep Space Nine", "Janeway" -> "Voyager")
println(shipMap("Picard"))
//println(shipMap("ABC")) // missing keys

val archerShip = util.Try(shipMap("Archer")) getOrElse "Unknown"
print(archerShip)

var numbers: List[Int] = (1 to 20).toList

val store: List[Int] = numbers.filter(num => num % 3 == 0)

println(store)
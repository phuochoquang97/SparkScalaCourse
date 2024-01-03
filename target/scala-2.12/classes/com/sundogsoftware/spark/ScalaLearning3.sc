// functions

// format def <function name>(params name:type...) : return type = {}
def squareIt(x: Int) : Int = {
  x * x
}

def cubeIt(x: Int) : Int = {x * x * x}

println(squareIt(2))
print(cubeIt(3))


def equations(x: Int, y: Float) : Float = {
  x + y
}

println(equations(3, 7.8f))


def transform(x: Int, f: Int => Int) : Int = {
  f(x)
}

val result = transform(2, cubeIt)
println(result)

// define function without name
val ret1 = transform(3, x => x*x*x)

val ret2 = transform(10, x => {val y = x * 2; y * y} )

// ex: write a func converting a string to upper-case
// use that func
def converting(s: String, f: String => String) : String = {
  f(s)
}

//converting("abd12A", s.toUpperCase)

val ret3 = converting("abd12AC", s => s.toUpperCase())
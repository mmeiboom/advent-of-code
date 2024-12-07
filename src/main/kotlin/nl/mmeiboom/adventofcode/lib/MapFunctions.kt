package nl.mmeiboom.adventofcode.lib

fun Map<Point2D, Char>.print() {
    for (y in 0 until 10) {
        for (x in 0 until 10) {
            print(this.getValue(Point2D(x, y)))
        }
        println()
    }
    println("-------")
}
package nl.mmeiboom.adventofcode.lib

data class Matrix<T>(
    val points: Map<Point2D, T>
) {
    companion object {

        fun from(input: List<String>) = from(input, { it })

        fun <T> from(input: List<String>, fn: (char: Char) -> T): Matrix<T> {
            val map: MutableMap<Point2D, T> = mutableMapOf()
            input.forEachIndexed { y, line ->
                line.forEachIndexed { x, char ->
                    map[Point2D(x, y)] = fn(char)
                }
            }
            return Matrix(map)
        }
    }
}

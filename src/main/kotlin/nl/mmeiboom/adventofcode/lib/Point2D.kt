package nl.mmeiboom.adventofcode.lib

data class Point2D(val x: Int, val y: Int) : Comparable<Point2D> {

    constructor(x: Long, y: Long) : this(x.toInt(), y.toInt())

    fun distance(other: Point2D) = Point2D(x - other.x, y - other.y)

    fun plus(x: Int, y: Int) = Point2D(this.x + x, this.y + y)

    operator fun plus(other: Pair<Int, Int>) = Point2D(x + other.first, y + other.second)
    operator fun plus(other: Point2D) = Point2D(x + other.x, y + other.y)
    operator fun plus(direction: Direction) = when (direction) {
        Direction.NORTH -> Point2D(x, y - 1)
        Direction.EAST -> Point2D(x + 1, y)
        Direction.SOUTH -> Point2D(x, y + 1)
        Direction.WEST -> Point2D(x - 1, y)
    }

    override fun compareTo(other: Point2D) = if (y == other.y) x.compareTo(other.x) else y.compareTo(other.y)

    fun manhattan(other: Point2D) = Math.abs(x - other.x) + Math.abs(y - other.y)
    fun manhattan() = manhattan(Point2D(0, 0))

    fun inBound(maxX: Int, maxY: Int) = inBound(0, maxX, 0, maxY)

    fun inBound(minX: Int, maxX: Int, minY: Int, maxY: Int) =
        x in minX..maxX && y in minY..maxY

    fun neighborsHv() = NEIGHBORS_HV.map { Point2D(it.x + this.x, it.y + this.y) }
    fun neighborsH() = NEIGHBORS_H.map { Point2D(it.x + this.x, it.y + this.y) }
    fun neighborsV() = NEIGHBORS_V.map { Point2D(it.x + this.x, it.y + this.y) }
    fun neighbors() = NEIGHBORS.map { Point2D(it.x + this.x, it.y + this.y) }

    fun directionTo(other: Point2D) = when {
        other.x == this.x && other.y < this.y -> Direction.NORTH
        other.x == this.x && other.y > this.y -> Direction.SOUTH
        other.y == this.y && other.x < this.x -> Direction.WEST
        other.y == this.y && other.x > this.x -> Direction.EAST
        else -> throw IllegalArgumentException("No NSEW direction between $this and $other")
    }

    fun rotate90() = Point2D(-y, x)

    fun down(amount: Int = 1) = copy(y = y + amount)
    fun up(amount: Int = 1) = copy(y = y - amount)
    fun left(amount: Int = 1) = copy(x = x - amount)
    fun right(amount: Int = 1) = copy(x = x + amount)

    companion object {
        fun parse(v: String, r: Regex) = tryParse(v, r)
            ?: throw IllegalArgumentException("Can't parse $v with regex ${r.pattern}")

        fun tryParse(v: String, r: Regex) =
            r.matchEntire(v)
                ?.groupValues
                ?.drop(1)
                ?.let { (x, y) -> Point2D(x.toInt(), y.toInt()) }

        fun of(v: String) = parse(v, DEFAULT_PARSE_REGEX)
        val NEIGHBORS = (-1..1)
            .flatMap { x -> (-1..1).map { y -> Point2D(x, y) } }
            .filterNot { it.x == 0 && it.y == 0 }

        val NEIGHBORS_H = listOf(Point2D(-1, 0), Point2D(1, 0))
        val NEIGHBORS_V = listOf(Point2D(0, -1), Point2D(0, 1))
        val NEIGHBORS_HV = NEIGHBORS_H + NEIGHBORS_V

        private val DEFAULT_PARSE_REGEX = "(-?[0-9]+)[,:; ]+(-?[0-9]+)".toRegex()
    }
}
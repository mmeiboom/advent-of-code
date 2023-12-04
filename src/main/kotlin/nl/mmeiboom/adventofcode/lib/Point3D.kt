package nl.mmeiboom.adventofcode.lib

import kotlin.math.abs

data class Point3D(val x: Int, val y: Int, val z: Int) {

    companion object {
        val ZERO = Point3D(0, 0, 0)
        val DIRECT_NEIGHBOURS = setOf(
            Point3D(1, 0, 0),
            Point3D(-1, 0, 0),
            Point3D(0, 1, 0),
            Point3D(0, -1, 0),
            Point3D(0, 0, 1),
            Point3D(0, 0, -1),
        )

        fun of(line: String): Point3D {
            return line.split(",").let { (x, y, z) ->
                Point3D(x.toInt(), y.toInt(), z.toInt())
            }
        }
    }

    fun manDist(b: Point3D) = abs(x - b.x) + abs(y - b.y) + abs(z - b.z)
    fun manDist() = abs(x) + abs(y) + abs(z)

    operator fun plus(b: Point3D) = Point3D(x + b.x, y + b.y, z + b.z)
    operator fun minus(b: Point3D) = Point3D(x - b.x, y - b.y, z - b.z)
    operator fun times(scale: Int) = Point3D(x * scale, y * scale, z * scale)

    operator fun get(i: Int) = when (i) {
        0 -> x
        1 -> y
        2 -> z
        else -> throw IndexOutOfBoundsException()
    }

    fun neighbours(): Set<Point3D> {
        return DIRECT_NEIGHBOURS.map { this.plus(it) }.toSet()
    }
}
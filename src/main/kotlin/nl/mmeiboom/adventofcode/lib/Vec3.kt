package nl.mmeiboom.adventofcode.lib

import kotlin.math.abs

data class Vec3(val x: Int, val y: Int, val z: Int) {

    companion object {
        val ZERO = Vec3(0, 0, 0)
    }

    fun manDist(b: Vec3) = abs(x - b.x) + abs(y - b.y) + abs(z - b.z)
    fun manDist() = abs(x) + abs(y) + abs(z)

    operator fun plus(b: Vec3) = Vec3(x + b.x, y + b.y, z + b.z)
    operator fun minus(b: Vec3) = Vec3(x - b.x, y - b.y, z - b.z)
    operator fun times(scale: Int) = Vec3(x * scale, y * scale, z * scale)

    operator fun get(i: Int) = when(i) {
        0 -> x
        1 -> y
        2 -> z
        else -> throw IndexOutOfBoundsException()
    }
}
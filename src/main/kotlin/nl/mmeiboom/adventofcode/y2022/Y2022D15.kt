package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.resourceLines
import kotlin.math.abs
import kotlin.math.max


object Y2022D15 : Day {

    private val sensors = resourceLines(2022, 15).parse()


    override fun part1(): Any {
        val y = 2000000
        val leftMost = sensors.minOf { it.point.left(it.scanRadius()).x }
        val rightMost = sensors.maxOf { it.point.right(it.scanRadius()).x }
        val beaconsOnLine = sensors.filter { it.beacon.y == y }.distinctBy { it.beacon }.count()
        val scannablePoints = IntRange(leftMost, rightMost).count { inScanRange(Point2D(it, y)) }
        return scannablePoints - beaconsOnLine
    }

    override fun part2(): Any {
        for (y in 0 until 4000001) {
            val ranges = sensors
                .mapNotNull { scannableRange(it, y) }
                .sortedBy { it.first }
            var current = 0
            ranges.forEach {
                if(it.first > current) {
                    return current.toLong() * 4000000L + y.toLong()
                }
                current = max(current, it.second + 1)
            }
        }
        throw Error("No distress beacon found within search range")
    }

    private fun scannableRange(sensor: Sensor, y: Int) : Pair<Int, Int>? {
        val del = abs(sensor.point.y - y)
        val dist = sensor.scanRadius()
        if (del > dist) return null
        return Pair(sensor.point.x - dist + del, sensor.point.x + dist - del)
    }

    private fun inScanRange(point: Point2D): Boolean {
        return sensors.any {
            it.point.manhattan(point) <= it.scanRadius()
        }
    }

    data class Sensor(val point: Point2D, val beacon: Point2D) {
        fun scanRadius(): Int = point.manhattan(beacon)
    }

    private fun List<String>.parse(): List<Sensor> {
        return this.map {
            val result = regex.toRegex().find(it) ?: throw Error("$it did not match")
            val xS = result.groupValues[1].toInt()
            val yS = result.groupValues[2].toInt()
            val xB = result.groupValues[3].toInt()
            val yB = result.groupValues[4].toInt()
            Sensor(Point2D(xS, yS), Point2D(xB, yB))
        }
    }

}

private const val regex = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)"

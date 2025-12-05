package nl.mmeiboom.adventofcode.y2015.day6

import nl.mmeiboom.adventofcode.lib.Matrix
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.Solution
import kotlin.math.max

class FireHazard(fileName: String?) : Solution<Triple<Toggle, Point2D, Point2D>, Int>(fileName) {

    override fun parse(line: String): Triple<Toggle, Point2D, Point2D> {
        val regex = Regex("(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)")
        val match = regex.find(line)!!
        val (toggle, x1, y1, x2, y2) = match.destructured

        val p1 = Point2D(x1.toInt(), y1.toInt())
        val p2 = Point2D(x2.toInt(), y2.toInt())

        return Triple(Toggle.of(toggle), p1, p2)
    }

    override fun solve1(data: List<Triple<Toggle, Point2D, Point2D>>): Int {
        val enabledLights = mutableSetOf<Point2D>()
        data.forEach { (toggle, point1, point2) ->

            val lights = point1.squareWith(point2)

            when (toggle) {
                Toggle.ON -> enabledLights.addAll(lights)
                Toggle.OFF -> enabledLights.removeAll(lights)
                Toggle.TOGGLE -> {
                    val nowOn = enabledLights.intersect(lights)
                    val nowOff = lights - nowOn
                    enabledLights.removeAll(nowOn)
                    enabledLights.addAll(nowOff)
                }
            }
        }

        return enabledLights.size
    }

    override fun solve2(data: List<Triple<Toggle, Point2D, Point2D>>): Int {
        val brightness = mutableMapOf<Point2D, Int>()
        data.forEach { (toggle, point1, point2) ->

            val lights = point1.squareWith(point2)

            when (toggle) {
                Toggle.ON -> lights.forEach { brightness[it] = brightness.getOrElse(it) { 0 } + 1 }
                Toggle.OFF -> lights.forEach { brightness[it] = max(0, brightness.getOrElse(it) { 0 } - 1) }
                Toggle.TOGGLE -> lights.forEach { brightness[it] = brightness.getOrElse(it) { 0 } + 2 }
            }
        }

        val max = brightness.values.max()
        Matrix(brightness).writeToFile("lights.png", { (it.toDouble() / max.toDouble() * 255.toDouble()).toInt() })
        return brightness.values.sum()
    }

}

enum class Toggle {
    ON, OFF, TOGGLE;

    companion object {
        fun of(value: String): Toggle {
            return when (value) {
                "turn on" -> ON
                "turn off" -> OFF
                "toggle" -> TOGGLE
                else -> throw IllegalArgumentException("Unknown toggle $value")
            }
        }
    }
}

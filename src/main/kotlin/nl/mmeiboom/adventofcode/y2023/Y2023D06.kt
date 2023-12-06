package nl.mmeiboom.adventofcode.y2023

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines
import java.math.BigDecimal

object Y2023D06 : Day {

    val targets = resourceLines(2023, 6).parse()

    override fun part1() = targets.map { it.calculateMargin() }.reduce { acc, el -> acc * el }

    override fun part2() = BoatRaceTarget(59688274, BigDecimal(543102016641022)).calculateMargin()

    data class BoatRaceTarget(val length: Int, val target: BigDecimal) {
        fun calculateMargin(): Int {
            return IntRange(0, length).count { distanceAfter(holdTime = it.toBigDecimal()) > target }
        }

        private fun distanceAfter(holdTime: BigDecimal): BigDecimal {
            return (length.toBigDecimal().minus(holdTime)).times(holdTime)
        }
    }

    private fun List<String>.parse(): List<BoatRaceTarget> {
        val regex = Regex("(\\d+)")
        val times = regex.findAll(this.first()).map { it.groupValues[1].toInt() }.toList()
        val distances = regex.findAll(this.last()).map { it.groupValues[1].toInt() }.toList()
        return times.indices.map {
            BoatRaceTarget(times[it], distances[it].toBigDecimal())
        }
    }
}

fun main() {
    print(Y2023D06.part2())
}
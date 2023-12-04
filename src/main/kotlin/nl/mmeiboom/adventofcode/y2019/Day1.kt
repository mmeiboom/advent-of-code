import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines
import kotlin.math.max

object Day1 : Day {

    private val masses = resourceLines(2019, 1).map { it.toLong() }

    private fun fuelRequiredFor(mass: Long): Long {
        return (mass / 3) - 2
    }

    private fun totalFuelRequiredFor(mass: Long): Long {
        val f1 = fuelRequiredFor(mass)
        return if (f1 > 0) {
            f1 + totalFuelRequiredFor(f1)
        } else max(0, f1)
    }

    override fun part1() = masses.map { fuelRequiredFor(it) }.sum()

    override fun part2() = masses.map { totalFuelRequiredFor(it) }.sum()

}
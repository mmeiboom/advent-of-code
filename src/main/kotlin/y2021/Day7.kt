package y2021

import lib.Day
import lib.resourceString
import kotlin.math.absoluteValue

object Day7 : Day {

    private val input = resourceString(2021, 7)
        .split(',')
        .map { it.trim().toInt() }

    override fun part1(): Int {
        val min = input.minOrNull()!!
        val max = input.maxOrNull()!!

        return IntRange(min, max).minOf { p ->
            input.sumOf { (it - p).absoluteValue }
        }
    }

    override fun part2(): Int {
        val min = input.minOrNull()!!
        val max = input.maxOrNull()!!

        return IntRange(min, max).minOf { p ->
            input.sumOf { fuelFromTo(it, p) }
        }
    }

    private fun fuelFromTo(it: Int, p: Int): Int {
        val dist = (it - p).absoluteValue
        return (dist * (dist + 1)) / 2
    }
}

fun main() {
    println(Day7.part1())
    println(Day7.part2())
}

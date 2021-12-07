package y2021

import lib.Day
import lib.resourceLines
import lib.resourceString
import kotlin.math.absoluteValue

object Day7 : Day {

    private val input = resourceString(2021, 7)
        .split(',')
        .map { it.trim().toInt() }

    override fun part1() : Long {
        val min = input.minOrNull()!!
        val max = input.maxOrNull()!!

        return IntRange(min,max).map { p ->
            input.sumOf { (it - p).absoluteValue }
        }.minOrNull()!!.toLong()
    }

    override fun part2() : Long {
        val min = input.minOrNull()!!
        val max = input.maxOrNull()!!

        return IntRange(min,max).map { p ->
            input.sumOf { fuelFromTo(it, p) }
        }.minOrNull()!!.toLong()
    }

    private fun fuelFromTo(it: Int, p: Int) : Int{
        val dist = (it - p).absoluteValue
        return (dist * (dist + 1)) / 2
    }
}

fun main() {
    println(Day7.part1())
    println(Day7.part2())
}

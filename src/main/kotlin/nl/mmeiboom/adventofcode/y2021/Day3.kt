package nl.mmeiboom.adventofcode.y2021

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines

object Day3 : Day {

    private val input = resourceLines(2021, 3)

    override fun part1() : Long {
        val gammaString = IntRange(0, input[0].length - 1).map { pos ->
            val zeroCount = input.count { it[pos] == '0' }
            val oneCount = input.count { it[pos] == '1' }
            if(zeroCount > oneCount) '0' else '1'
        }.joinToString(separator = "")
        val gamma = Integer.parseInt(gammaString, 2).toLong()
        val epsilonString = gammaString.map { if(it == '0') '1' else '0'  }.joinToString(separator = "")
        val epsilon = Integer.parseInt(epsilonString,2).toLong()
        return gamma * epsilon
    }

    override fun part2() : Long {
        val oxy = valOxy()
        val co2 = valCo2()
        return oxy * co2
    }

    private fun valOxy(): Long {
        val values = input.toMutableList()
        var pos = 0
        while (values.size > 1 && pos < input[0].length) {
            val oneCount = values.count { it[pos] == '1' }
            val keepChar = if (oneCount >= values.size.toDouble() / 2) '1' else '0'
            values.removeIf { it[pos] != keepChar }
            pos++;
        }
        return Integer.parseInt(values.first(), 2).toLong()
    }

    private fun valCo2(): Int {
        val values = input.toMutableList()
        var pos = 0
        while (values.size > 1 && pos < input[0].length) {
            val zeroCount = values.count { it[pos] == '0' }
            val keepChar = if (zeroCount <= values.size.toDouble() / 2) '0' else '1'
            values.removeIf { it[pos] != keepChar }
            pos++;
        }
        return Integer.parseInt(values.first(), 2)
    }

}

fun main() {
//    println(Day3.part1())
    println(Day3.part2())
}

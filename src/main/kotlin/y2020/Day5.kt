package y2020

import lib.Day
import lib.resourceLines

object Day5 : Day {

    private val input = resourceLines(2020, 5)

    override fun part1() = input.map { seatId(it) }.maxOf{ it }

    override fun part2() : Long {
        val sortedSeats = input.map { seatId(it) }.sorted()
        var prev = 0L
        for (seatId in sortedSeats)
        {
            if (seatId - prev == 2L) {
                return seatId - 1
            }
            prev = seatId
        }

        return -1
    }

    private fun seatId(s: String) = binToDec(s
            .replace('F', '0')
            .replace('B', '1')
            .replace('L', '0')
            .replace('R', '1')
            .toLong())
}

fun binToDec(num: Long): Long {
    var num = num
    var decimalNumber = 0L
    var i = 0
    var remainder: Long

    while (num.toInt() != 0) {
        remainder = num % 10
        num /= 10
        decimalNumber += (remainder * Math.pow(2.0, i.toDouble())).toInt()
        ++i
    }
    return decimalNumber
}

fun main() {
    println(Day5.part1())
    println(Day5.part2())
}
package y2020

import lib.Day
import lib.combinations
import lib.resourceLines
import kotlin.math.min
import kotlin.math.max

object Day9 : Day {

    private val input = resourceLines(2020, 9).map { it.toLong() }
    private const val preamble = 25

    override fun part1() : Long {
        var position = preamble

//        input.filterIndexed{ i, _ -> validCheckSum(i) }.first()

        while(position < input.size) {
            if(!validCheckSum(position)) {
                return input[position]
            }
            position++
        }

        return -1
    }

    private fun validCheckSum(position: Int): Boolean {
        return input.subList(position - preamble, position).combinations(2)
                .map { c -> c[0] +  c[1] }
                .contains(input[position])
    }

    override fun part2() : Long {
        val target = 675280050L
        var position = 0
        while(position < input.size) {
            var end = position + 1
            var sum = input[position]
            var min = input[position]
            var max = input[position]
            while(sum < target) {
                sum += input[end]
                min = min(min, input[end])
                max = max(max, input[end])
                end++
            }
            if(sum == target) {
                return min + max
            }
            position++
        }

        return -1
    }
}

fun main() {
    println(Day9.part1())
    println(Day9.part2())
}
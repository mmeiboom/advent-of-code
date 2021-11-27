package y2020

import lib.Day
import lib.resourceLines

object Day15 : Day {

    private val input = listOf(0,3,6)

    override fun part1() : Long {
        val startingNumbers = listOf(0,3,6)
        val spoken = mutableMapOf<Int,Int>()
        var last = -1
        var number = -1
        for(turn in 1..2020) {
            // Staring numbers first
            if(turn < startingNumbers.size) {
                number = startingNumbers[turn -1]
                spoken[number] = 1
                continue
            }

            if(spoken[last] == 1) {
                number = 0
                spoken[0] = spoken.getOrDefault(0, 0)
            }


            last = number
        }
        return last.toLong()
    }

    override fun part2() : Long {
        return -1
    }
}

fun main() {
    println(Day15.part1())
    println(Day15.part2())
}
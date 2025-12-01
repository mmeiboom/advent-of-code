package nl.mmeiboom.adventofcode.y2015.day1

import nl.mmeiboom.adventofcode.lib.Solution

class NotQuiteLisp(fileName: String?) : Solution<String, Int>(fileName) {
    override fun parse(line: String): String {
        return line
    }

    override fun solve1(data: List<String>): Int {
        var current = 0
        val instructions = data.single()
        instructions.forEach {
            when (it) {
                '(' -> current++
                ')' -> current--
            }
        }

        return current
    }

    override fun solve2(data: List<String>): Int {
        var current = 0
        var i = 0
        val instructions = data.single()
        while (current >= 0) {
            when (instructions[i]) {
                '(' -> current++
                ')' -> current--
            }
            i++
        }

        return i
    }
}
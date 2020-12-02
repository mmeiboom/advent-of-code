package y2020

import lib.Day
import lib.combinations
import lib.resourceLines

object Day2 : Day {

    private val entries = resourceLines(2020, 2)

    override fun part1(): Int {
        val regex = """(\d+)-(\d+) (\w): (\w+)""".toRegex()
        val valid = mutableListOf<String>()
        entries.forEach {
            val matchResult = regex.find(it)
            val (min, max, char, pass) = matchResult!!.destructured
            val freq = freq(char[0], pass)
            if(freq >= min.toInt() && freq <= max.toInt()) {
                valid.add(pass)
            }
        }

        return valid.size

    }

    override fun part2(): Int {
        val regex = """(\d+)-(\d+) (\w): (\w+)""".toRegex()
        val valid = mutableListOf<String>()
        entries.forEach {
            val matchResult = regex.find(it)
            val (min, max, char, pass) = matchResult!!.destructured
            val firstChar = pass[min.toInt() - 1]
            val secondChar = pass[max.toInt() - 1]
            if((firstChar == char[0] || secondChar == char[0]) && firstChar != secondChar )
            {
                valid.add(pass)
            }
        }

        return valid.size

    }

    fun freq(char: Char, string : String) : Int {
        var frequency = 0
        for (element in string) {
            if (char == element) {
                ++frequency
            }
        }
        return frequency
    }
}

fun main() {
    println(Day2.part1())
    println(Day2.part2())
}
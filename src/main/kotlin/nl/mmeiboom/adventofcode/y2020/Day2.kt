package nl.mmeiboom.adventofcode.y2020

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines

object Day2 : Day {

    private val entries = resourceLines(2020, 2)

    override fun part1(): Int {
        return entries.filter {
            val policy = parse(it)
            val count = count(policy.char, policy.password)
            count >= policy.min && count <= policy.max
        }.size
    }

    override fun part2(): Int {
        return entries.filter {
            val policy = parse(it)
            val password = policy.password
            val firstChar = password[policy.min - 1]
            val secondChar = password[policy.max - 1]
            (firstChar == password[0] || secondChar == password[0]) && firstChar != secondChar
        }.size
    }

    data class PasswordPolicy(val min: Int, val max: Int, val char: Char, val password: String)

    private fun parse(string: String): PasswordPolicy {
        val regex = """(\d+)-(\d+) (\w): (\w+)""".toRegex()
        val matchResult = regex.find(string)
        val (min, max, char, pass) = matchResult!!.destructured
        return PasswordPolicy(min.toInt(), max.toInt(), char[0], pass)
    }

    private fun count(char: Char, string: String) = string.toCharArray().filter { it == char }.size

}

fun main() {
    println(Day2.part1())
    println(Day2.part2())
}
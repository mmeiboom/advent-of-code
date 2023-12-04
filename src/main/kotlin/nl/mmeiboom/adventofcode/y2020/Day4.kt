package nl.mmeiboom.adventofcode.y2020

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines
import nl.mmeiboom.adventofcode.lib.splitByDoubleNewLine

object Day4 : Day {

    private val input = resourceLines(2020, 4)

    override fun part1() = input.splitByDoubleNewLine()
            .map { toPassport(it) }
            .filter { it.isComplete() }
            .size

    override fun part2() = input.splitByDoubleNewLine()
            .map { toPassport(it) }
            .filter { it.isComplete() }
            .filter { it.isValid() }
            .size

    private fun toPassport(strings: List<String>): Passport {
        val keys = strings.flatMap { it.split(" ") }
                .map { it.split(":")[0] to it.split(":")[1] }
                .toMap()

        return Passport(keys)
    }

    data class Passport(val keys: Map<String, String>) {
        private val requiredKeys = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

        fun isComplete() = keys.keys.containsAll(requiredKeys)

        fun isValid(): Boolean {
            if (!isComplete()) {
                return false
            }

            val validByr = keys["byr"]!!.toInt() in 1920..2002
            val validIyr = keys["iyr"]!!.toInt() in 2010..2020
            val validEyr = keys["eyr"]!!.toInt() in 2020..2030
            val validCm = keys["hgt"]!!.takeLast(2) == "cm" && keys["hgt"]!!.dropLast(2).toInt() in 150..193
            val validIn = keys["hgt"]!!.takeLast(2) == "in" && keys["hgt"]!!.dropLast(2).toInt() in 59..76
            val validHgt = validCm || validIn
            val validHcl = keys["hcl"]!!.matches("#[0-9a-f]{6}".toRegex());
            val validEcl = "amb blu brn gry grn hzl oth".split(" ").contains(keys["ecl"]!!)
            val validPid = keys["pid"]!!.matches("[0-9]{9}".toRegex());

            return validIyr
                    && validByr
                    && validEyr
                    && validHgt
                    && validHcl
                    && validEcl
                    && validPid
        }
    }
}

fun main() {
    println(Day4.part1())
    println(Day4.part2())
}
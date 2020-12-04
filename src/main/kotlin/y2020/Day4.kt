package y2020

import lib.Day
import lib.resourceLines

object Day4 : Day {

    private val input = resourceLines(2020, 4)

    override fun part1() = input.toPassports()
            .filter { it.isComplete() }
            .size

    override fun part2() = input.toPassports()
            .filter { it.isComplete() }
            .filter { it.isValid() }
            .size

    private fun List<String>.toPassports(): List<Passport> {
        val passports = mutableListOf<Passport>()
        val keys = mutableMapOf<String, String>()
        for (line in this) {
            if (line.isBlank()) {
                passports.add(Passport(keys.toMap()))
                keys.clear()
            } else {
                line.split(" ").forEach {
                    val (k, v) = it.split(":")
                    keys[k] = v
                }
            }
        }

        if (keys.isNotEmpty()) {
            passports.add(Passport(keys.toMap()))
        }

        return passports.toList()
    }

    data class Passport(
            val keys: Map<String, String>
    ) {
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
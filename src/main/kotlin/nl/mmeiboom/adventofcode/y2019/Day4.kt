import nl.mmeiboom.adventofcode.lib.Day

object Day4 : Day {

    private val input = (367479..893698).map { it.toString() }.toList()

    override fun part1() = input
            .filter { isIncreasing(it) }
            .filter { hasDouble(it) }
            .size

    override fun part2() = input
            .filter { isIncreasing(it) }
            .filter { hasStrictDouble(it) }
            .size

    private fun isIncreasing(password: String): Boolean {
        for (i in 1..5) {
            if (password[i] < password[i - 1]) {
                return false
            }
        }
        return true
    }

    private fun hasDouble(password: String): Boolean {
        for (i in 1..5) {
            if (password[i] == password[i - 1]) {
                return true
            }
        }
        return false
    }

    private fun hasStrictDouble(password: String): Boolean {
        for (i in 0..4) {
            if (password[i] == password[i + 1] && (i == 4 || (password[i + 2] != password[i])) && (i == 0 || (password[i - 1] != password[i]))) {
                return true
            }
        }
        return false
    }
}
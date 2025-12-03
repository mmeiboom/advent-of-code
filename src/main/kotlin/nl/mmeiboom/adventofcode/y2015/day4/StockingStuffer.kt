package nl.mmeiboom.adventofcode.y2015.day4

import nl.mmeiboom.adventofcode.lib.Solution
import nl.mmeiboom.adventofcode.lib.toHex
import java.security.MessageDigest

class StockingStuffer(fileName: String?) : Solution<String, Int>(fileName) {
    override fun parse(line: String): String {
        return line
    }

    override fun solve1(data: List<String>) = findDigestStartingWith(data, "00000")

    override fun solve2(data: List<String>) = findDigestStartingWith(data, "000000")

    private fun findDigestStartingWith(data: List<String>, prefix: String): Int {
        val md = MessageDigest.getInstance("MD5")
        val seed = data.single()
        var number = 0
        var done = false
        while (!done) {
            number++
            val digest = md.digest("$seed$number".toByteArray()).toHex()
            done = digest.startsWith(prefix)
        }
        return number
    }

}
package nl.mmeiboom.adventofcode.y2015.day8

import nl.mmeiboom.adventofcode.lib.Solution
import java.util.concurrent.ConcurrentHashMap

class MatchSticks(fileName: String?) : Solution<String, Int>(fileName) {

    val asciiRegex = Regex("\\\\x[0-f]{2}")

    override fun parse(line: String) = line

    override fun solve1(data: List<String>): Int {
        return data.sumOf { decode(it) }
    }

    override fun solve2(data: List<String>): Int {
        return data.sumOf { encode(it) }
    }

    private fun decode(s: String) : Int {
        val raw = s.drop(1).dropLast(1)
            .replace("\\\\","\\")
            .replace("\\\"","\"")
            .replace(asciiRegex, "'")
        return s.length - raw.length
    }

    private fun encode(s: String) : Int {
        val encoded = s
            .replace("\\","\\\\")
            .replace("\"","\\\"")
        val encodedAndQuoted = "\"$encoded\""

        return encodedAndQuoted.length - s.length
    }

}
package nl.mmeiboom.adventofcode.y2015.day5

import nl.mmeiboom.adventofcode.lib.Solution

class NaughtyOrNice(fileName: String?) : Solution<String, Int>(fileName) {

    private val vowels = setOf('a', 'e', 'i', 'o', 'u')
    private val blacklist = setOf(
        Pair('a', 'b'),
        Pair('c', 'd'),
        Pair('p', 'q'),
        Pair('x', 'y'),
    )

    private val repeatedPairMatcher = Regex("(..).*\\1")
    private val letterPairMatcher = Regex("(.).\\1")


    override fun parse(line: String) = line

    override fun solve1(data: List<String>): Int {
        return data.count { string ->
            val threeVowels = string.count { vowels.contains(it) } >= 3
            val oneDuplicate = string.zipWithNext().any { (first, second) -> first == second }
            val noBlacklistedPairs = string.zipWithNext().none { blacklist.contains(it) }
            threeVowels && oneDuplicate && noBlacklistedPairs
        }
    }

    override fun solve2(data: List<String>): Int {
        return data.count { string ->
            val repeatedPair = repeatedPairMatcher.containsMatchIn(string)
            val letterSandwich = letterPairMatcher.containsMatchIn(string)
            letterSandwich && repeatedPair
        }
    }
}
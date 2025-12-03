package nl.mmeiboom.adventofcode.y2025.day1

import nl.mmeiboom.adventofcode.lib.Solution

class SecretEntrance(fileName: String?) : Solution<Pair<Char, Long>, Long>(fileName) {
    override fun parse(line: String): Pair<Char, Long> = Pair(
        line.first(),
        line.slice(1..line.lastIndex).toLong()
    )

    override fun solve1(data: List<Pair<Char, Long>>): Long {
        var dial = 50L
        var count = 0L
        data.forEach { (dir, steps) ->
            val offset = steps % 100
            dial = if (dir == 'L') {
                (dial - offset) % 100
            } else {
                (dial + offset) % 100
            }

            if (dial == 0L) count++
        }

        return count
    }

    override fun solve2(data: List<Pair<Char, Long>>): Long {
        val disc = 100L
        var dial = 50L
        var count = 0L
        data.forEach { (dir, steps) ->
            count += steps.div(disc) // full rotations, count and  drop
            val offset = steps % disc // remainder
            if ((dial in 1 until offset && dir == 'L') || (dial > 0 && offset + dial > disc && dir == 'R')) {
                count++
            }

            dial = if (dir == 'L') {
                (dial - offset + 100) % 100
            } else {
                (dial + offset) % 100
            }

            if (dial == 0L) count++
        }

        return count
    }

}
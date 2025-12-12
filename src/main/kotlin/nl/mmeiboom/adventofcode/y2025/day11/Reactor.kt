package nl.mmeiboom.adventofcode.y2025.day11

import nl.mmeiboom.adventofcode.lib.Solution

class Reactor(fileName: String?) : Solution<Pair<String, List<String>>, Long>(fileName) {

    override fun parse(line: String): Pair<String, List<String>> {
        val regex = "(\\w+):(.*)".toRegex()
        val (from, to) = regex.matchEntire(line)!!.destructured
        return Pair(from, to.trim().split(' '))
    }

    override fun solve1(data: List<Pair<String, List<String>>>): Long {
        val map = data.associate { it.first to it.second }
        return dfs(map, "you", "out")
    }

    override fun solve2(data: List<Pair<String, List<String>>>): Long {
        val map = data.associate { it.first to it.second }
        return (
                dfs(map, "svr", "fft") * dfs(map, "fft", "dac") * dfs(map, "dac", "out") +
                        dfs(map, "svr", "dac") * dfs(map, "dac", "fft") * dfs(map, "fft", "out")
                )

    }

    private fun dfs(
        map: Map<String, List<String>>,
        from: String,
        to: String,
        memory: MutableMap<String, Long> = mutableMapOf(),
    ): Long {
        if (from == to) return 1L
        return memory.getOrPut(from) {
            map[from]?.sumOf { dfs(map, it, to, memory) } ?: 0L
        }
    }
}
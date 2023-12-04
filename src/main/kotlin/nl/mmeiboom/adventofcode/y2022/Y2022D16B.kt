package nl.mmeiboom.adventofcode.y2022

import com.github.shiguruikai.combinatoricskt.combinations
import com.github.shiguruikai.combinatoricskt.permutations
import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines

object Y2022D16B : Day {

    private val caves: Map<String, Cave> = resourceLines(2022, 16).map { Cave.of(it) }.associateBy { it.name }
    private val cheapestPathCosts: Map<String, Map<String, Int>> = calculateShortestPaths()

    override fun part1(): Any {
        return searchPaths("AA", 30)
    }

    override fun part2(): Any {
        return cheapestPathCosts.keys.filter { it != "AA" }
            .combinations(cheapestPathCosts.size / 2)
            .map { it.toSet() }
            .maxOf { halfOfTheRooms ->
                searchPaths("AA", 26, halfOfTheRooms) +
                        searchPaths("AA", 26, cheapestPathCosts.keys - halfOfTheRooms)
            }
    }

    private fun calculateShortestPaths(): Map<String, Map<String, Int>> {
        val shortestPaths = caves.values.associate {
            it.name to it.paths.associateWith { 1 }.toMutableMap()
        }.toMutableMap()

        shortestPaths.keys.permutations(3).forEach { (waypoint, from, to) ->
            shortestPaths[from, to] = minOf(
                shortestPaths[from, to], // Existing Path
                shortestPaths[from, waypoint] + shortestPaths[waypoint, to] // New Path
            )
        }
        val zeroFlowRooms = caves.values.filter { it.flowRate == 0 || it.name == "AA" }.map { it.name }.toSet()
        shortestPaths.values.forEach { it.keys.removeAll(zeroFlowRooms) }
        val canGetToFromAA: Set<String> = shortestPaths.getValue("AA").keys
        return shortestPaths.filter { it.key in canGetToFromAA || it.key == "AA" }
    }

    private operator fun Map<String, MutableMap<String, Int>>.set(key1: String, key2: String, value: Int) {
        getValue(key1)[key2] = value
    }

    private operator fun Map<String, Map<String, Int>>.get(key1: String, key2: String, defaultValue: Int = 31000): Int =
        get(key1)?.get(key2) ?: defaultValue

    private data class Cave(val name: String, val paths: List<String>, val flowRate: Int) {
        companion object {
            fun of(input: String): Cave =
                Cave(
                    input.substringAfter(" ").substringBefore(" "),
                    input.substringAfter("valve").substringAfter(" ").split(", "),
                    input.substringAfter("=").substringBefore(";").toInt()
                )
        }
    }


    private fun searchPaths(
        location: String,
        timeAllowed: Int,
        seen: Set<String> = emptySet(),
        timeTaken: Int = 0,
        totalFlow: Int = 0
    ): Int = cheapestPathCosts
        .getValue(location)
        .asSequence()
        .filterNot { (nextRoom, _) -> nextRoom in seen }
        .filter { (_, traversalCost) -> timeTaken + traversalCost + 1 < timeAllowed }
        .maxOfOrNull { (nextRoom, traversalCost) ->
            searchPaths(
                nextRoom,
                timeAllowed,
                seen + nextRoom,
                timeTaken + traversalCost + 1,
                totalFlow + ((timeAllowed - timeTaken - traversalCost - 1) * caves.getValue(nextRoom).flowRate)
            )
        } ?: totalFlow
}

fun main() {
    val p1 = Y2022D16B.part1()
    println("P1: $p1")
    val p2 = Y2022D16B.part2()
    println("P2: $p2")

}
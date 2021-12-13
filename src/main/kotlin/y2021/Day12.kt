package y2021

import lib.Day
import lib.resourceLines

object Day12 : Day {

    private val input = resourceLines(2021, 12)

    override fun part1(): Int {
        val vertices = parse(input)
        var routes = listOf(Route(listOf("start")))
        while (routes.any { it.isIncomplete() }) {
            routes = update(routes, vertices)
        }
        return routes.count()
    }

    class Route(val caves: List<String>) {

        fun isIncomplete(): Boolean {
            return caves.last() != "end"
        }

        fun neverVisitsSmallCavesTwice(): Boolean {
            return caves
                .filter { it.toLowerCase() == it }
                .groupBy { it }
                .none { it.value.size > 1 }
        }

        fun passedThrough(cave: String): Boolean {
            return caves.contains(cave)
        }

        fun position() = caves.last()

        override fun toString() : String {
            return caves.joinToString(separator = ",")
        }
    }

    private fun update(routes: List<Route>, vertices: Map<String, List<String>>): List<Route> {
        val newRoutes = mutableListOf<Route>()

        routes
            .forEach {
                if (it.isIncomplete()) {
                    vertices.getValue(it.position())
                        .filter { cave -> cave == cave.toUpperCase() || !it.passedThrough(cave) }
                        .forEach { cave ->
                            newRoutes.add(Route(it.caves + cave))
                        }
                } else {
                    newRoutes.add(it)
                }
            }

        return newRoutes.distinct()
    }

    private fun updateTwo(routes: List<Route>, vertices: Map<String, List<String>>): List<Route> {
        val newRoutes = mutableListOf<Route>()

        routes
            .forEach {
                if (it.isIncomplete()) {
                    vertices.getValue(it.position())
                        .filter { cave -> cave != "start" }
                        .filter { cave -> cave == cave.toUpperCase() || !it.passedThrough(cave) || it.neverVisitsSmallCavesTwice() }
                        .forEach { cave ->
                            newRoutes.add(Route(it.caves + cave))
                        }
                } else {
                    newRoutes.add(it)
                }
            }

        return newRoutes.distinct()
    }

    override fun part2(): Int {
        val vertices = parse(input)
        var routes = listOf(Route(listOf("start")))
        while (routes.any { it.isIncomplete() }) {
            routes = updateTwo(routes, vertices)
        }
        return routes.count()
    }

    fun parse(input: List<String>): Map<String, List<String>> {
        val pairs = input.flatMap {
            val (from, to) = it.split('-')
            listOf(Pair(from, to), Pair(to, from))
        }
        return pairs
            .map { (from, _) -> from }
            .distinct()
            .associateWith { from -> pairs.filter { it.first == from }.map { it.second } }
    }

}

fun main() {
    println(Day12.part1())
    println(Day12.part2())
}

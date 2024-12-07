package nl.mmeiboom.adventofcode.y2024

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Direction
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.print
import nl.mmeiboom.adventofcode.lib.resourceLines

object Y2024D06 : Day {

    val input = resourceLines(2024, 6).parse()


    override fun part1() : Long {
        val visited = mutableSetOf<Point2D>()
        val guard = Guard(input.entries.single { it.value == '^' }.key, Direction.NORTH)
        while(input.containsKey(guard.position)) {
            visited.add(guard.position)
            guard.move(input)
        }
        return visited.size.toLong()
    }

    override fun part2() = input.keys.count { loopsWithObstructionAt(it) }.toLong()

    private fun loopsWithObstructionAt(point: Point2D) : Boolean {
        val visited = mutableSetOf<Pair<Point2D, Direction>>()
        val guard = Guard(input.entries.single { it.value == '^' }.key, Direction.NORTH)
        val map = input.toMutableMap()
        map[point] = '#'
        while(map.containsKey(guard.position)) {
            val pair = Pair(guard.position, guard.direction)
            if(visited.contains(pair)) { return true }
            visited.add(pair)
            guard.move(map)
        }

        visited.forEach { map[it.first] = '+' }
        return false
    }

    private fun List<String>.parse(): Map<Point2D, Char> {
        val map: MutableMap<Point2D, Char> = mutableMapOf()
        this.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                map[Point2D(x, y)] = char
            }
        }
        return map
    }

    data class Guard(val startPosition: Point2D, val startDirection: Direction) {
        var position = startPosition
        var direction = startDirection

        fun move(map: Map<Point2D, Char>) {
            while(map[position.plus(direction)]?.equals('#') == true) {
                direction = direction.cw()
            }
            position = position.plus(direction)
        }
    }
}

fun main() {
    println(Y2024D06.part1())
    println(Y2024D06.part2())
}
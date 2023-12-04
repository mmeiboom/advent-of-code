package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Point3D
import nl.mmeiboom.adventofcode.lib.resourceLines

object Y2022D18 : Day {

    private val cubes = resourceLines(2022, 18).parse()

    override fun part1(): Any {
        return cubes.sumOf { it.surfaceArea() }
    }

    override fun part2(): Any {
        // determine reachable cubes
        val reachable = mutableSetOf<Point3D>()
        val checked = mutableSetOf<Point3D>()
        val toCheck = mutableListOf(Point3D(0, 0, 0))

        val minX = -1
        val minY = -1
        val minZ = -1
        val maxX = cubes.maxOf { it.x } + 1
        val maxY = cubes.maxOf { it.y } + 1
        val maxZ = cubes.maxOf { it.z } + 1

        while (toCheck.isNotEmpty()) {
            val cell = toCheck.removeFirst()
            if (checked.contains(cell)) continue

            cell.neighbours()
                .asSequence()
                .filter { it.x in minX..maxX }
                .filter { it.y in minY..maxY }
                .filter { it.z in minZ..maxZ }
                .filter { !checked.contains(it) }
                .filter { !cubes.contains(it) }
                .forEach { toCheck.add(it) }

            if (!cubes.contains(cell) && !reachable.contains(cell)) {
                reachable.add(cell)
            }

            checked.add(cell)
        }

        // count with additional condition
        return cubes.sumOf { it.reachableSurfaceArea(reachable) }
    }

    private fun Point3D.surfaceArea(): Int {
        return this.neighbours().count { !cubes.contains(it) }
    }

    private fun Point3D.reachableSurfaceArea(reachableCubes: Set<Point3D>): Int {
        return this.neighbours().count { !cubes.contains(it) && reachableCubes.contains(it) }
    }

    private fun List<String>.parse(): Set<Point3D> {
        return this.map { Point3D.of(it) }.toSet()
    }

}

fun main() {
    val p1 = Y2022D18.part1()
    println("P1: $p1")
    val p2 = Y2022D18.part2()
    println("P2: $p2")

}
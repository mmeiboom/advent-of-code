package nl.mmeiboom.adventofcode.y2025.day8

import nl.mmeiboom.adventofcode.lib.Point3D
import nl.mmeiboom.adventofcode.lib.Solution
import nl.mmeiboom.adventofcode.lib.UnionFind

class Playground(fileName: String?) : Solution<Point3D, Int>(fileName) {
    override fun parse(line: String): Point3D {
        return Point3D.of(line)
    }

    override fun solve1(data: List<Point3D>): Int {
        val circuit = UnionFind.new(data.size)

        weightedConnections(data)
            .take(1000)
            .forEach { (a, b) ->
                circuit.union(a, b)
            }

        return circuit.size.sortedDescending()
            .take(3)
            .reduce(Int::times)
    }

    override fun solve2(data: List<Point3D>): Int {
        val circuit = UnionFind.new(data.size)
        weightedConnections(data)
            .forEach { (a, b) ->
                circuit.union(a, b)
                val rootSize = circuit.size.max()
                if (rootSize == data.size) {
                    return data[a].x * data[b].x
                }
            }

        error("Unreachable code")
    }

    private fun weightedConnections(points: List<Point3D>) = sequence {
        val lengths = mutableListOf<Triple<Long, Int, Int>>()
        for (i in 0 until points.lastIndex) {
            for (j in i + 1 until points.size) {
                lengths.add(Triple(weight(points[i], points[j]), i, j))
            }
        }
        lengths.sortBy { it.first }
        for ((_, i, j) in lengths) {
            yield(i to j)
        }
    }

    private fun weight(p1: Point3D, p2: Point3D): Long {
        return ((p1.x - p2.x).toLong() * (p1.x - p2.x).toLong()
                + (p1.y - p2.y).toLong() * (p1.y - p2.y).toLong()
                + (p1.z - p2.z).toLong() * (p1.z - p2.z).toLong())
    }
}
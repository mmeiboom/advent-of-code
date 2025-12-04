import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Direction.*
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.resourceLines
import kotlin.math.min

object Day20 : Day {

    private val map = toPoints(resourceLines(2019, 20))

    override fun part1(): Any {
        val maze = Maze(map)
        return maze.findExit(start = Point2D(37, 112), exit = Point2D(75, 2))
//        return maze.findExit(startLocation = Point2D(9, 2), exitLocation = Point2D(13,16))
    }

    override fun part2(): Any {
        val maze = Maze(map)
        return maze.findExit(start = Point2D(37, 112), exit = Point2D(75, 2))
    }

    private fun toPoints(mapLines: List<String>): Map<Point2D, Char> {
        val points = mutableMapOf<Point2D, Char>()

        mapLines.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                points[Point2D(x, y)] = char
            }
        }

        return points
    }

    private fun Map<Point2D, Char>.toPortals(): Portals {
        val portals = mutableListOf<Portal>()

        this.forEach { (p, c) ->
            if (c == '.') {
                val entrance = p.neighborsHv().find { isPortalChar(getOrDefault(it, ' ')) }
                if (entrance != null) {
                    val portalPointTwo = entrance.neighborsHv().first { isPortalChar(getOrDefault(it, ' ')) }
                    val charOne = this[entrance]
                    val charTwo = this[portalPointTwo]

                    val label = when (entrance.directionTo(portalPointTwo)) {
                        NORTH, WEST -> "$charTwo$charOne"
                        EAST, SOUTH -> "$charOne$charTwo"
                    }
                    portals.add(Portal(entrance, p, label))
                }
            }
        }

        return Portals(portals)
    }


    private fun isPortalChar(char: Char?): Boolean {
        if (char == null) {
            return false
        }
        return char.toInt() >= 'A'.toInt() && char.toInt() <= 'Z'.toInt()
    }

    data class PointAtLevel(val point: Point2D, val level: Int) {
        val outer: Boolean = point.x < 9 || point.x > 100 || point.y < 9 || point.y > 100
    }

    data class Portals(val portals: List<Portal>) {
        fun replace(points: List<Point2D>): List<Point2D> {
            return points.map { replace(it) }
        }

        fun replace(p: Point2D): Point2D {
            val portal = portals.find { it.entrance == p } ?: return p
            val exit = portals.find { it != portal && it.label == portal.label } ?: return p
            return exit.exit
        }

        fun get(p: Point2D): Portal {
            return portals.first { it.entrance == p }
        }
    }

    data class Portal(val entrance: Point2D, val exit: Point2D, val label: String)

    data class Maze(val map: Map<Point2D, Char>) {

        private val distances: MutableMap<PointAtLevel, Int> = mutableMapOf()
        private val portals = map.toPortals()

        fun findExit(start: Point2D, exit: Point2D): Int {
            val startLocation = PointAtLevel(start, 0)
            val exitLocation = PointAtLevel(exit, 0)

            val previousLocations = mutableListOf<PointAtLevel>()
            var currentLocation = startLocation

            distances[currentLocation] = 0

            while (true) {
                val neighbours = neighbours(currentLocation)

                // Any path left to explore?
                val distanceToHere = distances.getValue(currentLocation)
                val backtracking = neighbours.none {
                    distances.getOrDefault(it, Int.MAX_VALUE) > distanceToHere + 1
                }

                if (backtracking && currentLocation == startLocation) {
                    return distances.getValue(exitLocation)
                }

                val nextLocation = neighbours.find {
                    distances.getOrDefault(it, Int.MAX_VALUE) > distanceToHere + 1
                } ?: previousLocations.removeAt(previousLocations.size - 1)

                val char = map[nextLocation.point]
                if (char != '.') {
                    distances[nextLocation] = -1
                } else {
                    if (!backtracking) {
                        previousLocations.add(currentLocation)
                        distances[nextLocation] = min(distances.getOrDefault(nextLocation, Int.MAX_VALUE), distanceToHere + 1)
                    }
                    currentLocation = nextLocation
                }
            }
        }

        private fun neighbours(currentLocation: PointAtLevel): List<PointAtLevel> {
            val points = currentLocation.point.neighborsHv()
            val neighbours = mutableListOf<PointAtLevel>()
            points.forEach { p ->
                val char = map.getOrDefault(p, '#')
                if (char == '#') {
                    return@forEach
                }
                if (char == '.') {
                    neighbours.add(PointAtLevel(p, currentLocation.level))
                }

                val afterPortal = portals.replace(p)
                if (afterPortal == p) {
                    neighbours.add(PointAtLevel(afterPortal, currentLocation.level))
                    return@forEach
                }

                if (currentLocation.outer) {
                    if (currentLocation.level == 0) {
                        // Portal closed
                        return@forEach
                    }

                    val neighbour = PointAtLevel(afterPortal, currentLocation.level - 1)
                    val label = portals.get(p).label
                    println("Jump through $label from $currentLocation to $neighbour")
                    neighbours.add(neighbour)
                } else {
                    if(currentLocation.level > 100) {
                        // Portal closed
                        return@forEach
                    }

                    val neighbour = PointAtLevel(afterPortal, currentLocation.level + 1)
                    neighbours.add(neighbour)
                }
            }

            return neighbours
        }
    }
}

fun main() {
    println(Day20.part1())
    println(Day20.part2())
}
import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Direction
import nl.mmeiboom.adventofcode.lib.IntCodeComputer
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.resourceString
import kotlin.math.min

object Day15 : Day {

    private val program = resourceString(2019, 15).split(",").map { it.toLong() }

    override fun part1(): Any {
        val robot = Robot()
        return robot.discover()
    }

    override fun part2(): Any {
        val robot = Robot()
        robot.discover()
        return robot.findMax()
    }

    class Robot {

        private var currentLocation = Point2D(0, 0)
        private val discoveredArea: MutableMap<Point2D, Int> = mutableMapOf(Pair(currentLocation, 0))
        private val computer = IntCodeComputer(program)

        fun discover(): Int {
            val locations = mutableListOf<Point2D>()

            while (true) {
                val backtracking = currentLocation.neighborsHv().none { !discoveredArea.containsKey(it) }
                val nextLocation = currentLocation.neighborsHv().find { !discoveredArea.containsKey(it) }
                    ?: locations.removeAt(locations.size - 1)

                val direction = currentLocation.directionTo(nextLocation)
                val input = when (direction) {
                    Direction.NORTH -> 1L
                    Direction.EAST -> 4L
                    Direction.SOUTH -> 2L
                    Direction.WEST -> 3L
                }

                computer.input.add(input)
                when (computer.runToNextOutput()) {
                    0L -> discoveredArea[nextLocation] = -1
                    1L -> {
                        if (!backtracking) {
                            locations.add(currentLocation)
                            discoveredArea[nextLocation] = min(
                                discoveredArea.getOrDefault(nextLocation, Int.MAX_VALUE),
                                discoveredArea.getValue(currentLocation) + 1
                            )
                        }
                        currentLocation = nextLocation
                    }

                    2L -> {
                        return min(
                            discoveredArea.getOrDefault(nextLocation, Int.MAX_VALUE),
                            discoveredArea.getValue(currentLocation) + 1
                        )
                    }
                }
            }
        }

        fun findMax(): Int {
            val locations = mutableListOf<Point2D>()
            val startLocation = currentLocation
            discoveredArea.clear()
            discoveredArea[currentLocation] = 0

            while (true) {
                val backtracking = currentLocation.neighborsHv().none { !discoveredArea.containsKey(it) }

                if (backtracking && currentLocation == startLocation) {
                    return discoveredArea.values.maxOrNull() ?: -1
                }

                val nextLocation = currentLocation.neighborsHv().find { !discoveredArea.containsKey(it) }
                    ?: locations.removeAt(locations.size - 1)

                val direction = currentLocation.directionTo(nextLocation)
                val input = when (direction) {
                    Direction.NORTH -> 1L
                    Direction.EAST -> 4L
                    Direction.SOUTH -> 2L
                    Direction.WEST -> 3L
                }

                computer.input.add(input)
                when (computer.runToNextOutput()) {
                    0L -> discoveredArea[nextLocation] = -1
                    1L, 2L -> {
                        if (!backtracking) {
                            locations.add(currentLocation)
                            discoveredArea[nextLocation] = min(
                                discoveredArea.getOrDefault(nextLocation, Int.MAX_VALUE),
                                discoveredArea.getValue(currentLocation) + 1
                            )
                        }
                        currentLocation = nextLocation
                    }
                }
            }
        }
    }
}

fun main() {
    println(Day15.part1())
    println(Day15.part2())
}

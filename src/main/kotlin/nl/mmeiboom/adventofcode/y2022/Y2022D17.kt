package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.Point2D
import nl.mmeiboom.adventofcode.lib.resourceString

object Y2022D17 : Day {

    private val rocks = generateRocks()
    private val chamber = Chamber()
    private val down = Point2D(0, -1)
    private val left = Point2D(-1, 0)
    private val right = Point2D(1, 0)
    private val jets = resourceString(2022, 17).parse()

    private var rocksCnt = 0
    private var jetsCnt = 0


    override fun part1(): Any {
        repeat(2022) {
            runSimulation()
        }
        return chamber.height()
    }

    private fun runSimulation() {
        var rock = rocks[rocksCnt % rocks.size].move(chamber.offset())
        while (true) {
            val jetDirection = jets[jetsCnt % jets.size]
            jetsCnt += 1
            val jetMove = rock.move(jetDirection)
            if (chamber.fits(jetMove)) {
                rock = jetMove
            }

            val downwardMove = rock.move(down)
            if (!chamber.fits(downwardMove)) {
                break
            }

            rock = downwardMove
        }
        rocksCnt += 1
        chamber.drop(rock)
    }

    data class State(val peaks: List<Int>, val rockMod: Int, val jetMod: Int)

    override fun part2(): Any {
        val rounds = 1000000000000L
        val previousStates = mutableMapOf<State, Pair<Int, Int>>()
        while (true) {
            runSimulation()
            val state = State(chamber.normalizedPeaks(), rocksCnt % rocks.size, jetsCnt % jets.size)
            if (state in previousStates) {
                // Cycle detected! Skip stuff
                val (rockCountAtLoopStart, heightAtLoopStart) = previousStates.getValue(state)
                val rocksPerLoop = rocksCnt - 1L - rockCountAtLoopStart
                val totalLoops = (rounds - rockCountAtLoopStart) / rocksPerLoop
                val remainder = (rounds - rockCountAtLoopStart) - (totalLoops * rocksPerLoop)
                val heightGainedSinceLoop = chamber.height() - heightAtLoopStart
                repeat(remainder.toInt()) {
                    runSimulation()
                }
                return chamber.height() + (heightGainedSinceLoop * (totalLoops - 1)) - 1
            }
            previousStates[state] = rocksCnt - 1 to chamber.height()
        }
    }

    class Chamber {
        private val chamber = mutableSetOf<Point2D>()
        private val leftWall = 0
        private val rightWall = 8
        private val bottom = 0

        fun fits(rock: Set<Point2D>): Boolean {
            return rock.none { chamber.contains(it) || it.x <= leftWall || it.x >= rightWall || it.y <= bottom }
        }

        fun drop(rock: Set<Point2D>) {
            chamber.addAll(rock)
        }

        fun offset(): Point2D = Point2D(3, height() + 4)

        fun height(): Int {
            return chamber.maxOfOrNull { it.y } ?: 0
        }

        fun normalizedPeaks(): List<Int> {
            val maxY = this.height()
            return IntRange(leftWall + 1, rightWall - 1)
                .map { x ->
                    val pointsInColumn = this.chamber.filter { it.x == x }
                    (pointsInColumn.maxOfOrNull { it.y } ?: 0) - maxY
                }
        }
    }

    private fun Set<Point2D>.move(offset: Point2D): Set<Point2D> =
        this.map { it + offset }.toSet()

    private fun String.parse(): List<Point2D> {
        return this.map {
            when (it) {
                '<' -> left
                '>' -> right
                else -> throw Error("Unexpected input parsing jet symbols")
            }
        }
    }

    private fun generateRocks(): List<Set<Point2D>> {
        return listOf(
            setOf(Point2D(0, 0), Point2D(1, 0), Point2D(2, 0), Point2D(3, 0)),
            setOf(Point2D(1, 0), Point2D(0, 1), Point2D(1, 1), Point2D(2, 1), Point2D(1, 2)),
            setOf(Point2D(0, 0), Point2D(1, 0), Point2D(2, 0), Point2D(2, 1), Point2D(2, 2)),
            setOf(Point2D(0, 0), Point2D(0, 1), Point2D(0, 2), Point2D(0, 3)),
            setOf(Point2D(0, 0), Point2D(1, 0), Point2D(0, 1), Point2D(1, 1))
        )
    }

}

fun main() {
    val p1 = Y2022D17.part1()
    println("P1: $p1")
    val p2 = Y2022D17.part2()
    println("P2: $p2")

}
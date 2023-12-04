package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines
import kotlin.math.max

object Y2022D16 : Day {

    private val input = resourceLines(2022, 16)

    override fun part1(): Any {
        return bestPath(30).psi
    }

    private fun bestPath(time: Int, eleph: Boolean = false): Path {
        val (fullMap, _) = input.parse()

        val paths = ArrayDeque(listOf(Path("AA", 0, 0, mutableMapOf("AA" to true), "AA-")))
        var bestPath = paths.first()
        while (paths.size > 0) {
            val path = paths.removeFirst()
            val curCave = fullMap[path.current]!!
            for (connection in curCave.simplifiedConnections) {
                if (connection.eleph != eleph) continue
                val openTime = path.time + connection.distance + 1
                if (openTime < time && !path.opened.getOrDefault(connection.to, false)) {
                    val newVisited = path.opened.toMutableMap()
                    newVisited[connection.to] = true
                    val newPsi = path.psi + (time - openTime) * fullMap[connection.to]!!.flow
                    paths.add(Path(connection.to, newPsi, openTime, newVisited, path.log + "${connection.to}-"))
                }

            }
            if (path.psi > bestPath.psi) bestPath = path
        }
        return bestPath
    }

    override fun part2(): Any {
        // https://github.com/Stefansfrank/advent_of_code_2022/blob/main/Day16.kt
        // This is still beyond me :mindblown:

        // Part 2 is solved by assuming that for the best result, the human and the elephant are handling
        // half of the working valves each. Si I loop through all permutations of splitting the working valve
        // caves in 2 equally sized list and mark them accordingly in all the rConn lists on each cave
        // I then run the pathfinding completely independent for the human and the elephant with drastically
        // complexity by ignoring all potential next caves that are not on the according list.
        val (fullMap, simplifiedMap) = input.parse()
        val rList = simplifiedMap.map { it.value }          // all working valves in an indexed list, so they are numbered
        val elIx = mutableMapOf<String, Boolean>()          // a map that is true for all caves on the elephants list
        var bestTotal = 0

        // this is a bit crude implementation of all permutations of splitting the list of working valves into 2
        // I basically count up through an Int until 2 to the power of the amount of working valves and
        // immediately discard numbers that do not have half of the bits sets
        val max = 1 shl rList.size
        var pct = 0
        for (i in 0 until max) {
            if (i.countOneBits() != simplifiedMap.size / 2) continue

            if((i * 100) / max > pct) {
                pct = (i * 100) / max
                println("$pct%")
            }

            rList.forEachIndexed { ix, cv -> elIx[cv.name] = ((i and (1 shl ix)) != 0) } // creating the elephant list
            rList.forEach { cv -> cv.simplifiedConnections.forEach { it.eleph = elIx[it.to]!! } }  // mark all the elephant caves
            fullMap["AA"]!!.simplifiedConnections.forEach { it.eleph = elIx[it.to]!! } // do that also for the target caves of AA

            val bestHumanPath = bestPath(26, false)
            val bestElephantPath = bestPath(26, true)

            bestTotal = max(bestTotal, bestHumanPath.psi + bestElephantPath.psi)
        }

        return bestTotal
    }

    data class Connection(
        val to: String,
        val distance: Int,
        var eleph: Boolean = false
    )

    data class Path(
        val current: String,
        val psi: Int,
        val time: Int,
        val opened: MutableMap<String, Boolean>,
        val log: String
    )

    data class Cave(
        val name: String,
        val flow: Int,
        val directConnections: List<Connection>,
        val simplifiedConnections: MutableList<Connection> = mutableListOf(),
    ) {
        // Iterate over all direct connections to find distances to other caves with working valves
        fun reduce(map: HashMap<String, Cave>, limit: Int) {
            var remaining = listOf(this)
            var distance = 0
            val found = mutableMapOf(name to true)

            // BFS
            while (true) {
                val newQue = mutableListOf<Cave>()
                distance += 1
                for (cave in remaining) {
                    for (conn in cave.directConnections) {
                        val next = map[conn.to]!!
                        if (next.flow > 0 && !found.getOrDefault(next.name, false)) {
                            this.simplifiedConnections.add(Connection(next.name, distance))
                            found[next.name] = true
                            if (this.simplifiedConnections.size == limit) return
                        }
                        newQue.add(next)
                    }
                }
                remaining = newQue
            }
        }
    }

    private fun List<String>.parse(): Pair<HashMap<String, Cave>, HashMap<String, Cave>> {
        val fulLMap = HashMap<String, Cave>()
        val simplifiedMap = HashMap<String, Cave>()

        // Parsing input
        val regex = "Valve ([A-Z]+) has flow rate=(\\d+); tunnels? leads? to valves? (.*)".toRegex()
        this.forEach { line ->
            val matches = regex.find(line)?.groupValues
            if (matches != null) {
                fulLMap[matches[1]] = Cave(
                    name = matches[1],
                    flow = matches[2].toInt(),
                    directConnections = matches[3].split(',').map { Connection(it.trim(), 1) }
                )
            }
        }

        // Build a list of only caves with working valves and compute the connections to this reduced set
        fulLMap.forEach { (name, cave) -> if (cave.flow > 0) simplifiedMap[name] = cave }
        simplifiedMap.forEach { (_, cave) -> cave.reduce(fulLMap, simplifiedMap.size - 1) }
        fulLMap["AA"]!!.reduce(fulLMap, simplifiedMap.size)

        return Pair(fulLMap, simplifiedMap)
    }

}

fun main() {
    val p1 = Y2022D16.part1()
    println("P1: $p1")
    val p2 = Y2022D16.part2()
    println("P2: $p2")

}
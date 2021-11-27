import lib.Day
import lib.resourceLines

object Day6 : Day {

    private val orbits = resourceLines(2019, 6).map { l -> l.split(")").let { (a, b) -> b to a } }.toMap()

    override fun part1() = orbits.keys.sumBy { countOrbits(it) }

    private fun countOrbits(objectInSpace: String): Int {
        var currentObject = objectInSpace
        var orbitsForObject = 0
        while (currentObject != "COM" && currentObject != "") {
            currentObject = orbits[currentObject].orEmpty()
            orbitsForObject++
        }
        return orbitsForObject
    }

    override fun part2(): Any {
        val pathToComForYou = path("YOU")
        val pathToComForSan = path("SAN")

        val sharedObjectsInPath = pathToComForSan.keys.intersect(pathToComForYou.keys)
        return sharedObjectsInPath.map { o -> pathToComForYou.getValue(o) + pathToComForSan.getValue(o) }.minOrNull() ?: -1
    }

    private fun path(objectInSpace: String): Map<String, Int> {
        val path = mutableMapOf<String, Int>()
        var currentObject = objectInSpace
        var distance = 0
        while (currentObject != "COM") {
            currentObject = orbits[currentObject].orEmpty()
            path.put(currentObject, distance)
            distance++
        }
        return path
    }
}

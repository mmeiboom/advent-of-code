import lib.Day
import lib.Vec3
import lib.combinations
import lib.lcm

object Day12 : Day {

    override fun part1(): Any {
        val system = fullSystem()
        system.simulate(steps = 10)
        return system.totalEnergy()
    }

    override fun part2() = fullSystem().findPeriod()

    private fun fullSystem() = Moons(listOf(Vec3(15, -2, -6), Vec3(-5, -4, -11), Vec3(0, -6, 0), Vec3(5, 9, 6)))
    private fun testSystem1() = Moons(listOf(Vec3(-1, 0, 2), Vec3(2, -10, -7), Vec3(4, -8, 8), Vec3(3, 5, -1)))
    private fun testSystem2() = Moons(listOf(Vec3(-8, -10, 0), Vec3(5, 5, 10), Vec3(2, -7, 3), Vec3(9, -8, -3)))

    data class Moons(val positions: List<Vec3>) {

        private val moons = positions.map { Moon(it) }
        private var t = 0

        private fun applyGravity() {
            val pairs = moons.combinations(2)

            for ((a, b) in pairs) {
                val (x, y, z) = IntArray(3) { i ->
                    a.position[i].compareTo(b.position[i])
                }
                val delta = Vec3(x, y, z)
                a.velocity -= delta
                b.velocity += delta
            }
        }

        private fun applyVelocity() = moons.forEach { it.position += it.velocity }

        fun totalEnergy() = moons.sumBy { it.energy() }

        fun simulate(steps: Int = 1) = IntRange(1, steps).let {
            applyGravity()
            applyVelocity()
            t += 1
        }

        fun findPeriod(): Long {
            val orbitTimes = IntArray(3)
            while (orbitTimes.contains(0)) {
                simulate()
                for (axis in 0 until 3) {
                    if (orbitTimes[axis] != 0) continue
                    if (moons.indices.all { moon -> atStart(moon, axis) }) {
                        orbitTimes[axis] = t
                    }
                }
            }

            return orbitTimes.fold(1L) { acc, i -> lcm(acc, i.toLong()) }
        }

        private fun atStart(moon: Int, axis: Int): Boolean {
            val mi = positions[moon]
            val mc = moons[moon]
            return mi[axis] == mc.position[axis] && mc.velocity[axis] == 0
        }
    }

    data class Moon(var position: Vec3, var velocity: Vec3 = Vec3(0, 0, 0)) {
        fun energy(): Int = position.manDist() * velocity.manDist()
    }
}


fun main() {
    println(Day12.part1())
    println(Day12.part2())
}
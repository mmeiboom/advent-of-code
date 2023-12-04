import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.IntCodeComputer
import nl.mmeiboom.adventofcode.lib.resourceString

object Day23 : Day {

    private val program = resourceString(2019, 23).split(",").map { it.toLong() }

    override fun part1(): Any {
        val network = Network()
        return network.runToIdle()?.x ?: "?"
    }

    override fun part2(): Any {
        return "nyi"
    }

    class Network {
        val computers = initializeNetwork()
        var running = false
        var nat: Packet? = null

        var loop = 0L
        var lastLoopIdle = 0L

        private fun initializeNetwork(): List<NetworkComputer> {
            return LongRange(0, 49).map { NetworkComputer(it) }
        }

        fun runToIdle(): Packet? {
            running = true

            while (running) {
                var idle = false

                computers.forEach {
                    it.computer.doCalculation()
                    if (it.computer.output.size >= 3) {
                        deliver(Packet(
                                it.computer.output.removeAt(0).toInt(),
                                it.computer.output.removeAt(0),
                                it.computer.output.removeAt(0)
                        ))
                    }
                }
            }

            return nat
        }

        private fun deliver(packet: Packet) {
            if (packet.address == 255) {
                running = false
                nat = packet
            } else {
                computers[packet.address].computer.input.add(packet.x)
                computers[packet.address].computer.input.add(packet.y)
            }
        }
    }

    data class Packet(val address: Int, val x: Long, val y: Long)

    data class NetworkComputer(val address: Long) {
        val computer = IntCodeComputer(program).also {
            it.input.add(address)
        }
    }

}

fun main() {
    println(Day23.part1())
    println(Day23.part2())
}
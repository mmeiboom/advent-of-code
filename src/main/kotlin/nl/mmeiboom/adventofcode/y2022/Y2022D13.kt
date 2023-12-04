package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines

// https://todd.ginsberg.com/post/advent-of-code/2022/day13/
object Y2022D13 : Day {

    private val packets = resourceLines(2022, 13)
        .filter { it.isNotBlank() }
        .map { Packet.of(it) }

    override fun part1(): Any {
        return packets.chunked(2).mapIndexed { index, pair ->
            if (pair.first() < pair.last()) index + 1 else 0
        }.sum()
    }

    override fun part2(): Any {
        val dividerPacket1 = Packet.of("[[2]]")
        val dividerPacket2 = Packet.of("[[6]]")
        val ordered = (packets + dividerPacket1 + dividerPacket2).sorted()
        return (ordered.indexOf(dividerPacket1) + 1) * (ordered.indexOf(dividerPacket2) + 1)
    }

    private sealed class Packet : Comparable<Packet> {

        companion object {

            fun of(input: String): Packet =
                of(
                    input.split("""((?<=[\[\],])|(?=[\[\],]))""".toRegex())
                        .filter { it.isNotBlank() }
                        .filter { it != "," }
                        .iterator()
                )

            private fun of(input: Iterator<String>): Packet {
                val packets = mutableListOf<Packet>()
                while (input.hasNext()) {
                    when (val symbol = input.next()) {
                        "]" -> return ListPacket(packets)
                        "[" -> packets.add(of(input))
                        else -> packets.add(IntPacket(symbol.toInt()))
                    }
                }
                return ListPacket(packets)
            }
        }
    }

    private class IntPacket(val amount: Int) : Packet() {
        fun asList(): Packet = ListPacket(listOf(this))

        override fun compareTo(other: Packet): Int =
            when (other) {
                is IntPacket -> amount.compareTo(other.amount)
                is ListPacket -> asList().compareTo(other)
            }
    }

    private class ListPacket(val subPackets: List<Packet>) : Packet() {
        override fun compareTo(other: Packet): Int =
            when (other) {
                is IntPacket -> compareTo(other.asList())
                is ListPacket -> subPackets.zip(other.subPackets)
                    .map { it.first.compareTo(it.second) }
                    .firstOrNull { it != 0 } ?: subPackets.size.compareTo(other.subPackets.size)
            }
    }

}
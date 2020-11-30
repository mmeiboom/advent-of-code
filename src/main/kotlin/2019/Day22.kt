import Day22.Technique.*
import lib.Day
import lib.resourceLines
import java.math.BigInteger

object Day22 : Day {

    val instructions = parse(resourceLines(2019, 22))

    override fun part1(): Any {
        val deck = LongRange(0, 10007 - 1).toList()
        val shuffler = Shuffler(deck)
        instructions.forEach {
            shuffler.use(it)
        }

        return shuffler.deck.indexOf(2019)
    }

    override fun part2(): Any {
        return "NYI" //day22b(resourceLines(2019, 22))

        // TODO
        // Instead of shuffling cards, can we backtrace a specific index to the initial one?
        // Probably too slow, solutions suggest a linear composition? modular inverse?
//        var index = 2020L
//        instructions.reversed().forEach {
//            println("Applying ${it.technique} to index $index")
//            index = backtrack(it, index)
//        }
//
//        return index
    }

    private fun backtrack(instruction: Instruction, index: Long): Long {
        val deckSize = 119315717514047L

        // CUT
        if (instruction.technique == CUT) {
            val cutSize = if (instruction.param > 0) instruction.param.toLong() else deckSize + instruction.param
            if (cutSize > 0) {
                if (cutSize >= index) {
                    return deckSize - cutSize + index
                }
                return index - cutSize
            }
            return index
        }

        // DEAL
        if (instruction.technique == DEAL_NEW_STACK) {
            return deckSize - index
        }

        // CUT
        if (instruction.technique == DEAL_INCREMENT) {
            val a = BigInteger.valueOf(index * instruction.param)
            val m = BigInteger.valueOf(deckSize)
            return a.modInverse(m).toLong()
        }

        error("Unknown instruction")
    }

    data class Instruction(val technique: Technique, val param: Int)

    data class Shuffler(var deck: List<Long>) {

        fun use(instruction: Instruction) {
            // CUT
            if (instruction.technique == CUT) {
                val n = if (instruction.param > 0) instruction.param else deck.size + instruction.param
                val topN = deck.take(n)
                val bottom = deck.drop(n)
                deck = bottom + topN
            }

            // DEAL
            if (instruction.technique == DEAL_NEW_STACK) {
                deck = deck.reversed()
            }

            // DEAL_INCREMENT
            if (instruction.technique == DEAL_INCREMENT) {
                val newDeck = MutableList(size = deck.size) { -1L }
                for (i in deck.indices) {
                    val fi = (instruction.param * i) % deck.size
                    newDeck[fi] = deck[i]
                }
                deck = newDeck
            }
        }
    }

    enum class Technique {
        CUT,
        DEAL_INCREMENT,
        DEAL_NEW_STACK
    }

    private fun parse(resourceLines: List<String>): List<Instruction> {
        return resourceLines.map {
            if (it.contains("deal into new stack")) {
                Instruction(DEAL_NEW_STACK, 0)
            } else if (it.contains("cut")) {
                Instruction(CUT, it.substring(4).toInt())
            } else {
                Instruction(DEAL_INCREMENT, it.substring(20).toInt())
            }
        }
    }
}

fun main() {
    println(Day22.part1())
    println(Day22.part2())
}
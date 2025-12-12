package nl.mmeiboom.adventofcode.y2019.lib

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InstructionTest {

    @Test
    fun `break down int to instruction`() {
        val instruction = Instruction(1002)
        assertEquals(Opcode.MULT, instruction.code())
        assertEquals(Mode.POSITION, instruction.mode(1))
        assertEquals(Mode.IMMEDIATE, instruction.mode(2))
        assertEquals(Mode.POSITION, instruction.mode(3))
    }

}
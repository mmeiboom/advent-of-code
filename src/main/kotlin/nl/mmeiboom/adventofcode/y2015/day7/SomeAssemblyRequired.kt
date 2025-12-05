package nl.mmeiboom.adventofcode.y2015.day7

import nl.mmeiboom.adventofcode.lib.Solution
import java.util.concurrent.ConcurrentHashMap

class SomeAssemblyRequired(fileName: String?) : Solution<CircuitNode, Int>(fileName) {
    override fun parse(line: String): CircuitNode {

        val regex = Regex("(.*) -> (\\w+)")
        val match = regex.find(line)!!
        val (function, output) = match.destructured
        if (function.contains("AND")) {
            val (left, right) = function.split(" AND ")
            return BinaryNode(output, left, right, { x, y -> x and y })
        } else if (function.contains("OR")) {
            val (left, right) = function.split(" OR ")
            return BinaryNode(output, left, right, { x, y -> x or y })
        } else if (function.contains("NOT")) {
            val input = function.substringAfter("NOT ")
            return UnaryNode(output, input, { x -> x.inv() })
        } else if (function.contains("LSHIFT")) {
            val (input, dist) = function.split(" LSHIFT ")
            return UnaryNode(output, input, { x -> x shl dist.toInt() })
        } else if (function.contains("RSHIFT")) {
            val (input, dist) = function.split(" RSHIFT ")
            return UnaryNode(output, input, { x -> x shr dist.toInt() })
        } else {
            val value = function.toIntOrNull()
            if(value == null) {
                return UnaryNode(output, function, { x -> x })
            } else {
                return StaticNode(output, value)
            }
        }
    }

    override fun solve1(data: List<CircuitNode>): Int {
        val circuit = Circuit(data.associateBy { it.output })
        return circuit.measure("a")
    }

    override fun solve2(data: List<CircuitNode>): Int {
        val adjustedNodes = data.associateBy { it.output }
            .toMutableMap()
        adjustedNodes["b"] = StaticNode("b", 16076)
        val circuit = Circuit(adjustedNodes)
        return circuit.measure("a")
    }

}

abstract class CircuitNode(val output: String) {
    abstract fun measure(circuit: Map<String, CircuitNode>, memory: MutableMap<String, Int>): Int
}

class StaticNode(output: String, val input: Int) : CircuitNode(output) {
    override fun measure(circuit: Map<String, CircuitNode>, memory: MutableMap<String, Int>): Int {
        memory[output] = input
        return input
    }
}

class UnaryNode(output: String, val input: String, val fn: (Int) -> Int) : CircuitNode(output) {
    override fun measure(circuit: Map<String, CircuitNode>, memory: MutableMap<String, Int>): Int {
        if (memory.contains(output)) return memory[output]!!

        val value = input.toIntOrNull() ?: circuit.getValue(input).measure(circuit, memory)
        val r = fn(value)
        memory[output] = r
        return r
    }
}

class BinaryNode(output: String, val left: String, val right: String, val fn: (Int, Int) -> Int) : CircuitNode(output) {
    override fun measure(circuit: Map<String, CircuitNode>, memory: MutableMap<String, Int>): Int {
        if (memory.contains(output)) return memory[output]!!

        val x = left.toIntOrNull() ?: circuit.getValue(left).measure(circuit, memory)
        val y = right.toIntOrNull() ?: circuit.getValue(right).measure(circuit, memory)
        val r = fn(x, y)
        memory[output] = r
        return r
    }
}

class Circuit(
    val nodes: Map<String, CircuitNode>
) {
    private val memory = ConcurrentHashMap<String, Int>().toMutableMap()

    fun measure(node: String): Int {
        return nodes.getValue(node).measure(nodes, memory)
    }
}
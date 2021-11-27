package lib

import java.io.InputStream
import java.time.Duration
import java.util.stream.Collectors

private val HEX_CHARS = "0123456789abcdef".toCharArray()

fun resource(year: Int, day: Int): InputStream {
    val name = String.format("/%d/day%02d.txt", year, day)

    return {}.javaClass.getResourceAsStream(name) ?: throw IllegalArgumentException("No resource with name " + name)
}

fun resourceString(year: Int, day: Int): String {
    val name = String.format("/%d/day%02d.txt", year, day)
    return {}.javaClass.getResource(name).readText()
}

fun resourceLines(year: Int, day: Int): List<String> {
    return resource(year, day).bufferedReader().lines().collect(Collectors.toList())
}

fun resourceRegex(year: Int, day: Int, regex: Regex): List<List<String>> {
    val lines = resourceLines(year, day)

    val misMatch = lines.filter { !regex.matches(it) }

    if (!misMatch.isEmpty()) {
        misMatch.forEach { println(it) }
        println("${misMatch.size} lines don't match regex ${regex.pattern}")
    }

    return lines.map { regex.matchEntire(it)!!.groupValues }.toList()
}

fun resourceRegex(year: Int, day: Int, regex: Map<String, Regex>) = resourceLines(year, day).map { matchRegex(it, regex) }

private fun matchRegex(line: String, regex: Map<String, Regex>) : Pair<String, List<String>> {
    val matches = regex.entries.map { it.key to it.value.matchEntire(line) }.filter { it.second != null }
    if(matches.size != 1) {
        throw RuntimeException("Wrong number of matchers for '$line': ${matches.size}")
    }

    return matches.map { it.first to it.second!!.groupValues }.first()
}

fun stringToDigits(s: String): List<Int> {
    if (!s.matches(Regex("[0-9]+"))) {
        throw IllegalArgumentException("s does not match [0-9]+")
    }

    return s.toCharArray().map { c -> c - '0' }
}

fun join(numbers: List<Int>): String {
    return numbers.joinToString(",")
}

fun factorial(num: Int) = (1..num).fold(1, { a, b -> a * b })

fun slices(line: String, len: Int) = (0..line.length - len)
        .map { line.slice(it until it + len) }

fun testRegex(regex: Regex, lines: List<String>) {
    val mismatching = lines.count { !regex.matches(it) }
    lines.filter { !regex.matches(it) }.forEach { println(it) }

    println("${lines.size} lines, matching: ${lines.size - mismatching}, not matching: $mismatching")
}

fun ByteArray.toHex(): String {
    val result = StringBuffer()

    forEach {
        val octet = it.toInt()
        val firstIndex = (octet and 0xF0).ushr(4)
        val secondIndex = octet and 0x0F
        result.append(HEX_CHARS[firstIndex])
        result.append(HEX_CHARS[secondIndex])
    }

    return result.toString()
}

fun List<Int>.toHex(): String {
    val result = StringBuffer()

    forEach {
        if(it > 255) {
            throw IllegalArgumentException("$it larger than 255")
        }
        val firstIndex = (it and 0xF0).ushr(4)
        val secondIndex = it and 0x0F
        result.append(HEX_CHARS[firstIndex])
        result.append(HEX_CHARS[secondIndex])
    }

    return result.toString()
}

fun String.isInt() = matches(Regex("-?[0-9]+"))

fun <T> rotateLeft(list: List<T>, amount: Int): List<T> {
    val amt = amount % list.size

    return list.subList(amt, list.size) + list.subList(0, amt)
}

fun <T> rotateRight(list: List<T>, amount: Int): List<T> {
    val amt = amount % list.size

    return list.subList(list.size - amt, list.size) + list.subList(0, list.size - amt)
}

fun <T> swap(list: List<T>, a: Int, b: Int): List<T> {
    list.indices.map { if(it == a) b else if(it == b) a else it }

    return list.slice(list.indices.map { if(it == a) b else if(it == b) a else it })
}

fun <T> swapByValue(list: List<T>, a: T, b: T) = swap(list, list.indexOf(a), list.indexOf(b))

fun formatDuration(ms: Long): String {
    val d = Duration.ofMillis(ms)
    if (ms > 60000) {
        return String.format("%s m %s s", d.toMinutes(), d.minusMinutes(d.toMinutes()).seconds)
    } else if (ms > 10000) {
        return String.format("%s s", d.seconds)
    } else if (ms > 1000) {
        return String.format("%.2f s", ms / 1000.0)
    } else {
        return String.format("%s ms", ms)
    }
}

fun reverse(list: MutableList<Int>, index: Int, length: Int) {
    val indices = (index until index + length).map { it % list.size }
    val subList = list.slice(indices).reversed()
    indices.forEachIndexed { i, v -> list[v] = subList[i] }
}

fun xor(list: List<Int>) = list.fold(0, { a, b -> a xor  b })

fun toBinary(hex: String) = hex.map { it.toString().toInt(16).toString(2).padStart(4, '0') }.joinToString("")

fun Int.toBinary() = toString(2).padStart(32, '0')

fun List<Any>.parallelMap(threads: Int, func: (a: Any) -> Any): List<Any> {

    return this.map { func(it) }
}

fun combine(ranges: List<LongRange>) : List<LongRange> {
    val list = mutableListOf<LongRange>()

    ranges.forEach { r ->
        val others: List<LongRange> = list.filter { overlap(r, it) }

        list.removeAll(others)
        val combined = others + listOf(r)
        list += combined.minOf { it.first } .. combined.maxOf { it.last }
    }

    return list.sortedBy { it.first }
}

fun overlap(a: LongRange, b: LongRange) : Boolean {
    return a.contains(b.first)
            || a.contains(b.last)
            || b.contains(a.first)
            || b.contains(a.last) || a.last + 1 == b.first || b.last + 1 == a.first
}

fun <T> permute(list: List<T>): List<List<T>> {
    if (list.size == 1) return listOf(list)
    val perms = mutableListOf<List<T>>()
    val sub = list[0]
    for (perm in permute(list.drop(1)))
        for (i in 0..perm.size) {
            val newPerm = perm.toMutableList()
            newPerm.add(i, sub)
            perms.add(newPerm)
        }
    return perms
}


fun gcd(a: Int, b: Int): Int = gcd(a.toLong(), b.toLong()).toInt()
fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b

fun <T> Iterable<T>.combinations(length: Int): Sequence<List<T>> =
        sequence {
            @Suppress("UNCHECKED_CAST")
            val pool = this as? List<T> ?: toList()
            val n = pool.size
            if (length > n) return@sequence
            val indices = IntArray(length) { it }
            while (true) {
                yield(indices.map { pool[it] })
                var i = length
                do {
                    i--
                    if (i == -1) return@sequence
                } while (indices[i] == i + n - length)
                indices[i]++
                for (j in i + 1 until length) indices[j] = indices[j - 1] + 1
            }
        }

fun main(args: Array<String>) {
    val list = (0..5).toList()

    println(rotateLeft(list, 20))
    println(rotateRight(list, 20))
}

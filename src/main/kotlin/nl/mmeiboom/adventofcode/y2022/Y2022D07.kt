package nl.mmeiboom.adventofcode.y2022

import nl.mmeiboom.adventofcode.lib.Day
import nl.mmeiboom.adventofcode.lib.resourceLines

object Y2022D07 : Day {

    val input = resourceLines(2022, 7)
    private val fileSizeRegex = "(\\d+) (.*)".toRegex()

    override fun part1(): Any {
        val root = input.parse()
        return root.listDirs()
            .map { it.size() }
            .filter { it <= 100000 }
            .sumOf { it }
    }

    override fun part2(): Any {
        val disk = 70000000
        val required = 30000000
        val root = input.parse()
        val free = disk - root.size()
        val toFree = required - free

        return root.listDirs()
            .map { it.size() }
            .filter { it >= toFree }
            .min()
    }

    data class File(val name: String, val size: Int)

    data class Directory(val name: String) {
        private val files = mutableListOf<File>()
        val dirs = mutableListOf<Directory>()
        var parent: Directory? = null

        fun add(dir: Directory) {
            dirs.add(dir)
            dir.parent = this
        }

        fun add(file: File) {
            files.add(file)
        }

        fun size(): Int =
            dirs.sumOf { it.size() } + files.sumOf { it.size }

        fun listDirs(): List<Directory> {
            val children = dirs.flatMap { it.listDirs() }
            return children + listOf(this)
        }
    }

    private fun List<String>.parse(): Directory {
        val root = Directory("/")
        var current = root
        this.forEach { line ->
            if (line == "$ cd /") {
                current = root
            } else if (line.startsWith("dir")) {
                val (_, name) = line.split(' ')
                current.add(Directory(name))
            } else if (line == "$ cd ..") {
                current = current.parent!!
            } else if (line.startsWith("$ cd")) {
                val (_, _, name) = line.split(' ')
                current = current.dirs.first { it.name == name }
            } else if (line.matches(fileSizeRegex)) {
                val (size, name) = line.split(' ')
                current.add(File(name, size.toInt()))
            }
        }
        return root
    }
}
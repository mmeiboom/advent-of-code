package nl.mmeiboom.adventofcode.lib

fun String.extractNumbers() = this.split(" ").map { it.toLong() }
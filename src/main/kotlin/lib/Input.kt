package lib

fun List<String>.splitByDoubleNewLine(): List<List<String>> {
    val groups = mutableListOf<List<String>>()
    val group = mutableListOf<String>()

    this.forEach { line ->
        if (line.isEmpty()) {
            groups.add(group.toList())
            group.clear()
        } else {
            group.add(line)
        }
    }

    if (group.isNotEmpty()) {
        groups.add(group)
    }

    return groups
}
package day12

data class Cave(val name: String, val isSmall: Boolean)

data class CaveBuilder(var name: String? = null) {

    fun build(): Cave {
        if (name == null || name == "") {
            throw IllegalStateException("Every Cave must have a non-null, non-empty name")
        }

        return Cave(name = name!!, isSmall = name == name!!.lowercase())
    }
}

data class Tunnel(val start: Cave, val end: Cave)


class TunnelBuilder(var start: Cave? = null, var end: Cave? = null) {

    fun start(init: CaveBuilder.() -> Unit) {
        start = CaveBuilder().also { it.init() }.build()
    }

    fun end(init: CaveBuilder.() -> Unit) {
        end = CaveBuilder().also { it.init() }.build()
    }

    fun build(): Tunnel {
        if (start == null) {
            throw IllegalStateException("Every Tunnel must have a non-null start")
        }

        if (end == null) {
            throw IllegalStateException("Every Tunnel must have a non-null end")
        }

        return Tunnel(start = start!!, end = end!!)
    }
}

data class CaveSystem(val tunnels: Map<Cave, Set<Cave>>, val start: Cave, val end: Cave) {

    fun adjacentCaves(cave: Cave) =
        tunnels.getOrDefault(cave, setOf())
}

data class CaveSystemBuilder(val tunnels: MutableMap<Cave, MutableSet<Cave>> = hashMapOf()) {

    fun tunnel(init: TunnelBuilder.() -> Unit) {
        val tunnel = TunnelBuilder().also { it.init() }.build()
        tunnels.getOrPut(tunnel.start) { hashSetOf() }.add(tunnel.end)
        tunnels.getOrPut(tunnel.end) { hashSetOf() }.add(tunnel.start)
    }

    fun build(): CaveSystem {
        val start = tunnels.keys.first { cave -> cave.name == "start" }
        val end = tunnels.keys.first { cave -> cave.name == "end" }
        return CaveSystem(tunnels = tunnels, start = start, end = end)
    }
}

fun caveSystem(init: CaveSystemBuilder.() -> Unit): CaveSystem {
    val caveSystemBuilder = CaveSystemBuilder()
    caveSystemBuilder.init()
    return caveSystemBuilder.build()
}

fun parseCaveSystem(input: List<String>): CaveSystem {
    return caveSystem {
        input.map { line ->
            val (startName, endName) = line.split('-')
            tunnel {
                start {
                    name = startName
                }

                end {
                    name = endName
                }
            }
        }
    }
}

data class Path(
    val currentCave: Cave,
    val visitedCaves: Set<Cave>,
    val canVisitSmallCaveAgain: Boolean
)

fun countAllPaths(caveSystem: CaveSystem, allowDoubleEntryToOneSmallCave: Boolean): Long {
    var numPaths = 0L
    val pathStack = ArrayDeque<Path>()
    pathStack.addFirst(
        Path(
            currentCave = caveSystem.start,
            visitedCaves = hashSetOf(caveSystem.start),
            canVisitSmallCaveAgain = allowDoubleEntryToOneSmallCave
        )
    )

    while (pathStack.isNotEmpty()) {
        val currentPath = pathStack.removeFirst()
        for (nextCave in caveSystem.adjacentCaves(currentPath.currentCave)) {
            if (nextCave == caveSystem.end) {
                numPaths += 1
                continue
            }

            if (nextCave == caveSystem.start) {
                continue
            }

            if (nextCave.isSmall && currentPath.visitedCaves.contains(nextCave)) {
                if (currentPath.canVisitSmallCaveAgain) {
                    val nextPath = currentPath.copy(
                        currentCave = nextCave,
                        canVisitSmallCaveAgain = false
                    )
                    pathStack.addFirst(nextPath)
                } else {
                    continue
                }
            } else {
                val nextPath = currentPath.copy(
                    currentCave = nextCave,
                    visitedCaves = currentPath.visitedCaves.plus(nextCave)
                )
                pathStack.addFirst(nextPath)
            }
        }

    }

    return numPaths
}

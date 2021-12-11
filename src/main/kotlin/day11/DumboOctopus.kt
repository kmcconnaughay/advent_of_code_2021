package day11

import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.data.set
import kotlin.math.max
import kotlin.math.min

private data class Coordinate(val row: Int, val column: Int)

data class SteppedOctopusGrid(val grid: D2Array<Int>, val numFlashes: Int)

fun findFirstSynchronizedGeneration(initialGrid: D2Array<Int>): Int {
    var currentGrid = SteppedOctopusGrid(initialGrid, numFlashes = 0)
    var generation = 0

    while (currentGrid.numFlashes != currentGrid.grid.size) {
        currentGrid = stepOctopusGrid(currentGrid.grid)
        generation += 1
    }

    return generation
}

fun simulateOctopusGrid(initialGrid: D2Array<Int>, numGenerations: Int): SteppedOctopusGrid {
    var totalNumFlashes = 0
    var currentGrid = SteppedOctopusGrid(grid = initialGrid, numFlashes = 0)
    for (generation in 1..numGenerations) {
        currentGrid = stepOctopusGrid(currentGrid.grid)
        totalNumFlashes += currentGrid.numFlashes
    }

    return SteppedOctopusGrid(currentGrid.grid, numFlashes = totalNumFlashes)
}

fun stepOctopusGrid(initialGrid: D2Array<Int>): SteppedOctopusGrid {
    val nextGrid = initialGrid.copy()
    val (numRows, numColumns) = nextGrid.shape

    val flashed: MutableSet<Coordinate> = HashSet()
    val flashStack = ArrayDeque<Coordinate>()

    for ((row, column) in nextGrid.multiIndices) {
        val octopus = Coordinate(row = row, column = column)
        if (nextGrid[octopus.row, octopus.column] >= 9 && !flashed.contains(octopus)) {
            flashStack.addFirst(octopus)
            flashed.add(octopus)
        } else {
            nextGrid[octopus.row, octopus.column] += 1
        }
    }

    while (flashStack.isNotEmpty()) {
        val currentOctopus = flashStack.removeFirst()
        val windowTop = max(0, currentOctopus.row - 1)
        val windowBottom = min(currentOctopus.row + 1, numRows - 1)
        val windowLeft = max(0, currentOctopus.column - 1)
        val windowRight = min(currentOctopus.column + 1, numColumns - 1)

        for (nextOctopus in cartesianProduct(windowTop..windowBottom, windowLeft..windowRight)) {
            if (nextGrid[nextOctopus.row, nextOctopus.column] >= 9 && !flashed.contains(nextOctopus)) {
                flashStack.addFirst(nextOctopus)
                flashed.add(nextOctopus)
            } else {
                nextGrid[nextOctopus.row, nextOctopus.column] += 1
            }
        }
    }

    for (octopus in flashed) {
        nextGrid[octopus.row, octopus.column] = 0
    }

    return SteppedOctopusGrid(grid = nextGrid, numFlashes = flashed.size)
}

private fun cartesianProduct(
    rowRange: IntRange,
    columnRange: IntRange
) = rowRange.flatMap { row ->
    columnRange.map { column ->
        Coordinate(
            row = row,
            column = column
        )
    }
}

fun parseOctopusGrid(input: List<String>): D2Array<Int> {
    if (input.isEmpty()) {
        throw IllegalArgumentException()
    }

    val numRows = input.size
    val numColumns = input[0].length
    val octopuses = input.flatMap { it.map { height -> height.digitToInt() } }
    return mk.ndarray(octopuses, dim1 = numRows, dim2 = numColumns)
}
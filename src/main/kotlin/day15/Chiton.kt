package day15

import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.data.set
import java.util.PriorityQueue

data class Coordinate(val row: Int, val column: Int)

fun computeLowestTotalRisk(chitons: D2Array<Int>): Int {
    val (numRows, numColumns) = chitons.shape
    val start = Coordinate(row = 0, column = 0)
    val end = Coordinate(row = numRows - 1, column = numColumns - 1)

    val risks = mk.zeros<Int>(dim1 = numRows, dim2 = numColumns)
    for ((row, column) in risks.multiIndices) {
        risks[row, column] = Int.MAX_VALUE
    }
    risks[start.row, start.column] = 0

    val coordinateQueue: PriorityQueue<Coordinate> = PriorityQueue(compareBy { risks[it.row, it.column] })
    coordinateQueue.add(start)
    val explored: MutableSet<Coordinate> = hashSetOf()

    while (coordinateQueue.isNotEmpty()) {
        val currentPosition = coordinateQueue.remove()
        if (currentPosition == end) {
            return risks[currentPosition.row, currentPosition.column]
        }

        if (explored.contains(currentPosition)) {
            continue
        } else {
            explored.add(currentPosition)
        }

        val newPositions = listOf(
            Coordinate(row = currentPosition.row - 1, column = currentPosition.column),
            Coordinate(row = currentPosition.row + 1, column = currentPosition.column),
            Coordinate(row = currentPosition.row, column = currentPosition.column - 1),
            Coordinate(row = currentPosition.row, column = currentPosition.column + 1),
        ).filter { !explored.contains(it) && it.row in 0 until numRows && it.column in 0 until numColumns }

        for (newPosition in newPositions) {
            coordinateQueue.add(newPosition)
            val newRisk =
                risks[currentPosition.row, currentPosition.column] + chitons[newPosition.row, newPosition.column]
            if (newRisk < risks[newPosition.row, newPosition.column]) {
                risks[newPosition.row, newPosition.column] = newRisk
                coordinateQueue.add(newPosition)
            }
        }
    }

    throw IllegalStateException()
}

fun tileCave(initialScan: D2Array<Int>): D2Array<Int> {
    val (numRows, numColumns) = initialScan.shape
    val result = mk.zeros<Int>(dim1 = 5 * numRows, dim2 = 5 * numColumns)

    for ((row, column) in result.multiIndices) {
        val originalValue = initialScan[row % numRows, column % numColumns]
        val increment = (row / numRows) + (column / numColumns)
        val newValue = originalValue + increment
        val wrappedValue = if (newValue > 9) {
            (newValue % 10) + 1
        } else {
            newValue
        }
        result[row, column] = wrappedValue
    }

    return result
}

fun parseChitonRiskLevels(input: List<String>): D2Array<Int> {
    val numRows = input.size
    val numColumns = input[0].length
    val octopuses = input.flatMap { it.map { riskLevel -> riskLevel.digitToInt() } }
    return mk.ndarray(octopuses, dim1 = numRows, dim2 = numColumns)
}
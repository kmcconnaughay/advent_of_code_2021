package day09

import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.data.set
import org.jetbrains.kotlinx.multik.ndarray.operations.map

data class Coordinate(val row: Int, val column: Int)

data class Basin(val coordinates: Set<Coordinate>)

fun findBasins(heightMap: D2Array<Int>): Set<Basin> {
    val (numRows, numColumns) = heightMap.shape
    val processed: D2Array<Int> = heightMap.map {
        if (it == 9) {
            1
        } else {
            0
        }
    }
    val basins: MutableSet<Basin> = HashSet()

    for ((row, column) in heightMap.multiIndices) {
        if (processed[row, column] == 1) {
            continue
        } else {
            processed[row, column] = 1
        }

        val coordinate = Coordinate(row, column)
        val queue = ArrayDeque<Coordinate>()
        val basinCoordinates: MutableSet<Coordinate> = HashSet()
        queue.addFirst(coordinate)
        while (queue.isNotEmpty()) {
            val current = queue.removeLast()
            basinCoordinates.add(current)

            val exploreTop = current.row > 0 && processed[current.row - 1, current.column] == 0
            if (exploreTop) {
                processed[current.row - 1, current.column] = 1
                queue.addFirst(Coordinate(row = current.row - 1, column = current.column))
            }

            val exploreBottom = current.row < numRows - 1 && processed[current.row + 1, current.column] == 0
            if (exploreBottom) {
                processed[current.row + 1, current.column] = 1
                queue.addFirst(Coordinate(row = current.row + 1, column = current.column))
            }

            val exploreLeft = current.column > 0 && processed[current.row, current.column - 1] == 0
            if (exploreLeft) {
                processed[current.row, current.column - 1] = 1
                queue.addFirst(Coordinate(row = current.row, column = current.column - 1))
            }

            val exploreRight = current.column < numColumns - 1 && processed[current.row, current.column + 1] == 0
            if (exploreRight) {
                processed[current.row, current.column + 1] = 1
                queue.addFirst(Coordinate(row = current.row, column = current.column + 1))
            }
        }
        basins.add(Basin(coordinates = basinCoordinates))
    }

    return basins
}

fun computeRiskLevels(heightMap: D2Array<Int>): D2Array<Int> {
    val (numRows, numColumns) = heightMap.shape
    if (numRows < 2 || numColumns < 2) {
        throw IllegalArgumentException()
    }
    val riskLevels: D2Array<Int> = mk.zeros(dim1 = numRows, dim2 = numColumns)

    for ((row, column) in heightMap.multiIndices) {
        var isLowPoint = true

        val compareTop = row > 0
        if (compareTop) {
            isLowPoint = isLowPoint && heightMap[row, column] < heightMap[row - 1, column]
        }

        val compareBottom = row < numRows - 1
        if (compareBottom) {
            isLowPoint = isLowPoint && heightMap[row, column] < heightMap[row + 1, column]
        }

        val compareLeft = column > 0
        if (compareLeft) {
            isLowPoint = isLowPoint && heightMap[row, column] < heightMap[row, column - 1]
        }

        val compareRight = column < numColumns - 1
        if (compareRight) {
            isLowPoint = isLowPoint && heightMap[row, column] < heightMap[row, column + 1]
        }

        if (isLowPoint) {
            riskLevels[row, column] = heightMap[row, column] + 1
        }
    }

    return riskLevels
}

fun parseHeightMap(input: List<String>): D2Array<Int> {
    if (input.isEmpty()) {
        throw IllegalArgumentException()
    }

    val numRows = input.size
    val numColumns = input[0].length
    val heights = input.flatMap { heights -> heights.map { height -> height.digitToInt() } }
    return mk.ndarray(heights, dim1 = numRows, dim2 = numColumns)
}
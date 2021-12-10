package day07

import kotlin.math.min

fun alignPositionsWithTriangularFuelConsumption(positionHistogram: List<Int>): Int {
    var minimumFuelConsumption = Integer.MAX_VALUE

    for (position in positionHistogram.indices) {
        var fuelToMoveRight = 0
        for (leftIndex in 0 until position) {
            fuelToMoveRight += positionHistogram[leftIndex] * triangularNumber(position - leftIndex)
        }

        var fuelToMoveLeft = 0
        for (rightIndex in (position + 1) until positionHistogram.size) {
            fuelToMoveLeft += positionHistogram[rightIndex] * triangularNumber(rightIndex - position)
        }

        minimumFuelConsumption = min(minimumFuelConsumption, fuelToMoveRight + fuelToMoveLeft)
    }

    return minimumFuelConsumption
}

fun alignPositionsWithLinearFuelConsumption(positionHistogram: List<Int>): Int {
    var numCrabsToLeft = 0
    var numCrabsToRight = positionHistogram.subList(1, positionHistogram.size).sum()
    var fuelToMoveRight = 0
    var fuelToMoveLeft =
        positionHistogram.mapIndexed { position, numCrabs -> position * numCrabs }.sum()
    var minimumFuelConsumption = fuelToMoveRight + fuelToMoveLeft

    for (i in 1 until positionHistogram.size) {
        numCrabsToLeft += positionHistogram[i - 1]
        fuelToMoveRight += numCrabsToLeft
        fuelToMoveLeft -= numCrabsToRight
        numCrabsToRight -= positionHistogram[i]
        minimumFuelConsumption = min(minimumFuelConsumption, fuelToMoveRight + fuelToMoveLeft)
    }

    return minimumFuelConsumption
}

fun createPositionHistogram(unparsedPositions: List<String>): List<Int> {
    val positions = unparsedPositions[0].split(",").map { it.toInt() }
    val maxPosition = positions.maxOrNull() ?: 0
    val positionHistogram = MutableList(maxPosition + 1) { 0 }

    for (position in positions) {
        positionHistogram[position] += 1
    }

    return positionHistogram
}

private fun triangularNumber(n: Int): Int {
    return n * (n + 1) / 2
}

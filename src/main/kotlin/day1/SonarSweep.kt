package day1

import java.io.FileReader

/**
 * Returns the number of strict increases in depth in the given measurements.
 *
 * This is Part 1 of the challenge. See the [challenge description](https://adventofcode.com/2021/day/1) for more
 * information.
 */
fun numDepthMeasurementIncreases(depthMeasurements: List<Int>): Int {
    return depthMeasurements.windowed(size = 2, step = 1, partialWindows = false)
        .count { (firstDepthMeasurement, secondDepthMeasurement) -> firstDepthMeasurement < secondDepthMeasurement }
}

fun numAveragedDepthMeasurementIncreases(depthMeasurements: List<Int>): Int {
    return depthMeasurements.windowed(size = 3, step = 1, partialWindows = false).map(List<Int>::sum)
        .windowed(size = 2, step = 1, partialWindows = false)
        .count { (firstAverage, secondAverage) -> firstAverage < secondAverage }
}

fun readDepthMeasurements(): List<Int> {
    val fileName =
        "C:\\Users\\Allie\\IdeaProjects\\advent_of_code_2021\\src\\main\\kotlin\\day1\\depth_measurements.txt"
    val fileReader = FileReader(fileName)
    return fileReader.readLines().map(Integer::valueOf)
}
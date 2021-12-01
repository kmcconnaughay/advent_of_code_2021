package day1

import java.io.FileReader
import java.nio.file.Path

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

/**
 * Returns the number of strict increases in depth from three-width rolling sums of the given measurements.
 *
 * This is Part 2 of the challenge. See the [challenge description](https://adventofcode.com/2021/day/1) for more
 * information.
 */
fun numAveragedDepthMeasurementIncreases(depthMeasurements: List<Int>): Int {
    return depthMeasurements.windowed(size = 3, step = 1, partialWindows = false).map(List<Int>::sum)
        .windowed(size = 2, step = 1, partialWindows = false)
        .count { (firstAverage, secondAverage) -> firstAverage < secondAverage }
}

/**
 * Marshals depth measurements from the given file. This procedure assumes that the file contains one integer per line
 * with no other text.
 */
fun readDepthMeasurements(fileName: Path): List<Int> {
    val fileReader = FileReader(fileName.toFile())
    return fileReader.readLines().map(Integer::valueOf)
}
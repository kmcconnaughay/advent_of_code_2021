package day01

/**
 * Returns the number of strict increases in depth in the given measurements.
 *
 * This is Part 1 of the challenge. See the [challenge description](https://adventofcode.com/2021/day/1) for more
 * information.
 */
fun numDepthMeasurementIncreases(depthMeasurements: List<String>): Int {
    return depthMeasurements.map(Integer::valueOf)
        .windowed(size = 2, step = 1, partialWindows = false)
        .count { (firstDepthMeasurement, secondDepthMeasurement) -> firstDepthMeasurement < secondDepthMeasurement }
}

/**
 * Returns the number of strict increases in depth from three-width rolling sums of the given measurements.
 *
 * This is Part 2 of the challenge. See the [challenge description](https://adventofcode.com/2021/day/1) for more
 * information.
 */
fun numAveragedDepthMeasurementIncreases(depthMeasurements: List<String>): Int {
    return depthMeasurements.map(Integer::valueOf).windowed(size = 3, step = 1, partialWindows = false)
        .map(List<Int>::sum)
        .windowed(size = 2, step = 1, partialWindows = false)
        .count { (firstAverage, secondAverage) -> firstAverage < secondAverage }
}

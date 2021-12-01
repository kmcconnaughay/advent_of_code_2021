import day1.numAveragedDepthMeasurementIncreases
import day1.numDepthMeasurementIncreases
import day1.readDepthMeasurements

fun main() {
    val depthMeasurements = readDepthMeasurements()
    println("Day 1 Part 1: numDepthMeasurementIncreases = ${numDepthMeasurementIncreases(depthMeasurements)}")
    println("Day 1 Part 2: numAveragedDepthMeasurementIncreases = ${numAveragedDepthMeasurementIncreases(depthMeasurements)}")
}

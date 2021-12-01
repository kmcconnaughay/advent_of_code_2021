import day1.numAveragedDepthMeasurementIncreases
import day1.numDepthMeasurementIncreases
import day1.readDepthMeasurements
import java.nio.file.Path

fun main() {
    val depthMeasurementsFileName = Path.of("C:\\Users\\Allie\\IdeaProjects\\advent_of_code_2021\\src\\main\\kotlin\\day1\\depth_measurements.txt")
    val depthMeasurements = readDepthMeasurements(depthMeasurementsFileName)
    println("Day 1 Part 1: numDepthMeasurementIncreases = ${numDepthMeasurementIncreases(depthMeasurements)}")
    println("Day 1 Part 2: numAveragedDepthMeasurementIncreases = ${numAveragedDepthMeasurementIncreases(depthMeasurements)}")
}

import day01.numAveragedDepthMeasurementIncreases
import day01.numDepthMeasurementIncreases
import day02.runCommands
import day02.runCommandsWithAim
import day03.computeLifeSupportRating
import day03.computePowerConsumption
import java.nio.file.Path

private const val projectDirectory = "C:\\Users\\Allie\\IdeaProjects\\advent_of_code_2021\\"
private const val kotlinDirectory = projectDirectory + "src\\main\\kotlin\\"

fun main() {
    val day1FileName = Path.of(kotlinDirectory + "day01\\depth_measurements.txt")
    val day1Data = readData(day1FileName)
    println("Day 01 Part 1: ${numDepthMeasurementIncreases(day1Data)}")
    println("Day 01 Part 2: ${numAveragedDepthMeasurementIncreases(day1Data)}")

    val day2FileName = Path.of(kotlinDirectory + "day02\\commands.txt")
    val day2Data = readData(day2FileName)
    val part1Position = runCommands(day2Data)
    println("Day 02 Part 1: ${part1Position.horizontalPosition * part1Position.depth}")
    val part2Position = runCommandsWithAim(day2Data)
    println("Day 02 Part 2: ${part2Position.horizontalPosition * part2Position.depth}")

    val day3FileName = Path.of(kotlinDirectory + "day03\\binary_diagnostic.txt")
    val day3Data = readData(day3FileName)
    println("Day 03 Part 1: ${computePowerConsumption(day3Data).powerConsumption}")
    println("Day 03 Part 2: ${computeLifeSupportRating(day3Data).lifeSupportRating}")
}

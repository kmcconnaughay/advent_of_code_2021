import day01.numAveragedDepthMeasurementIncreases
import day01.numDepthMeasurementIncreases
import day02.runCommands
import day02.runCommandsWithAim
import day03.computeLifeSupportRating
import day03.computePowerConsumption
import day04.BingoGame
import day05.countIntersections
import day05.parseLineSegment
import java.nio.file.Path

private const val projectDirectory = "C:\\Users\\Allie\\IdeaProjects\\advent_of_code_2021\\"
private const val kotlinDirectory = projectDirectory + "src\\main\\kotlin\\"

fun main() {
    val day01FileName = Path.of(kotlinDirectory + "day01\\depth_measurements.txt")
    val day01Data = readData(day01FileName)
    println("Day 01 Part 1: ${numDepthMeasurementIncreases(day01Data)}")
    println("Day 01 Part 2: ${numAveragedDepthMeasurementIncreases(day01Data)}")

    val day02FileName = Path.of(kotlinDirectory + "day02\\commands.txt")
    val day02Data = readData(day02FileName)
    val part1Position = runCommands(day02Data)
    println("Day 02 Part 1: ${part1Position.horizontalPosition * part1Position.depth}")
    val part2Position = runCommandsWithAim(day02Data)
    println("Day 02 Part 2: ${part2Position.horizontalPosition * part2Position.depth}")

    val day03FileName = Path.of(kotlinDirectory + "day03\\binary_diagnostic.txt")
    val day03Data = readData(day03FileName)
    println("Day 03 Part 1: ${computePowerConsumption(day03Data).powerConsumption}")
    println("Day 03 Part 2: ${computeLifeSupportRating(day03Data).lifeSupportRating}")

    val day04FileName = Path.of(kotlinDirectory + "day04\\squid_bingo.txt")
    val day04Data = readData(day04FileName)
    val bingoGame = BingoGame.parse(day04Data)
    println("Day 04 Part 1: ${bingoGame.runToFirstWinner()}")
    println("Day 04 Part 2: ${bingoGame.runToLastWinner()}")

    val day05FileName = Path.of(kotlinDirectory + "day05\\hydrothermal_venture.txt")
    val day05Data = readData(day05FileName)
    val lineSegments = day05Data.map(::parseLineSegment)
    println("Day 05 Part 1: ${countIntersections(lineSegments, onlyConsiderVerticalAndHorizontalLineSegments = true)}")
    println("Day 05 Part 2: ${countIntersections(lineSegments)}")
}

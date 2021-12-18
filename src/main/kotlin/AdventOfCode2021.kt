//import day18.maxPairwiseMagnitude
import day01.numAveragedDepthMeasurementIncreases
import day01.numDepthMeasurementIncreases
import day02.runCommands
import day02.runCommandsWithAim
import day03.computeLifeSupportRating
import day03.computePowerConsumption
import day04.BingoGame
import day05.countIntersections
import day05.parseLineSegment
import day06.initializePopulation
import day06.populationSizeAfterNSteps
import day07.alignPositionsWithLinearFuelConsumption
import day07.alignPositionsWithTriangularFuelConsumption
import day07.createPositionHistogram
import day08.countOnesFoursSevensAndEightsInOutputValues
import day08.sumOutputValues
import day09.computeRiskLevels
import day09.findBasins
import day09.parseHeightMap
import day10.computeAutoCompleteScore
import day10.computeSyntaxErrorScore
import day11.findFirstSynchronizedGeneration
import day11.parseOctopusGrid
import day11.simulateOctopusGrid
import day12.countAllPaths
import day12.parseCaveSystem
import day13.fold
import day13.followFoldInstructions
import day13.parseThermalCameraManual
import day13.plot
import day14.followPolymerizationInstructions
import day14.parsePolymerizationInstructions
import day15.computeLowestTotalRisk
import day15.parseChitonRiskLevels
import day15.tileCave
import day16.Packet
import day16.hexToBinary
import day16.sumOfVersionNumbers
import day17.countValidInitialVelocities
import day17.findMaximumAltitude
import day17.parseTargetArea
import day18.SnailfishNumber
import day18.maxPairwiseMagnitude
import day18.sum
import org.jetbrains.kotlinx.multik.ndarray.operations.sum

fun main() {
    val day01Data = readData(day = 1)
    println("Day 01 Part 1: ${numDepthMeasurementIncreases(day01Data)}")
    println("Day 01 Part 2: ${numAveragedDepthMeasurementIncreases(day01Data)}")

    val day02Data = readData(day = 2)
    val part1Position = runCommands(day02Data)
    println("Day 02 Part 1: ${part1Position.horizontalPosition * part1Position.depth}")
    val part2Position = runCommandsWithAim(day02Data)
    println("Day 02 Part 2: ${part2Position.horizontalPosition * part2Position.depth}")

    val day03Data = readData(day = 3)
    println("Day 03 Part 1: ${computePowerConsumption(day03Data).powerConsumption}")
    println("Day 03 Part 2: ${computeLifeSupportRating(day03Data).lifeSupportRating}")

    val day04Data = readData(day = 4)
    val bingoGame = BingoGame.parse(day04Data)
    println("Day 04 Part 1: ${bingoGame.runToFirstWinner()}")
    println("Day 04 Part 2: ${bingoGame.runToLastWinner()}")

    val day05Data = readData(day = 5)
    val lineSegments = day05Data.map(::parseLineSegment)
    println("Day 05 Part 1: ${countIntersections(lineSegments, onlyConsiderVerticalAndHorizontalLineSegments = true)}")
    println("Day 05 Part 2: ${countIntersections(lineSegments)}")

    val day06Data = readData(day = 6)
    println("Day 06 Part 1: ${populationSizeAfterNSteps(initializePopulation(day06Data), 80)}")
    println("Day 06 Part 2: ${populationSizeAfterNSteps(initializePopulation(day06Data), 256)}")

    val day07Data = readData(day = 7)
    println("Day 07 Part 1: ${alignPositionsWithLinearFuelConsumption(createPositionHistogram(day07Data))}")
    println("Day 07 Part 2: ${alignPositionsWithTriangularFuelConsumption(createPositionHistogram(day07Data))}")

    val day08Data = readData(day = 8)
    println("Day 08 Part 1: ${countOnesFoursSevensAndEightsInOutputValues(day08Data)}")
    println("Day 08 Part 2: ${sumOutputValues(day08Data)}")

    val day09Data = readData(day = 9)
    val heightMap = parseHeightMap(day09Data)
    println("Day 09 Part 1: ${computeRiskLevels(heightMap).sum()}")
    val basins = findBasins(heightMap)
    println("Day 09 Part 2: ${
        basins.map { it.coordinates.size }.sortedDescending().subList(0, 3)
            .reduce { acc, basinSize -> acc * basinSize }
    }"
    )

    val day10Data = readData(day = 10)
    println("Day 10 Part 1: ${computeSyntaxErrorScore(day10Data)}")
    println("Day 10 Part 2: ${computeAutoCompleteScore(day10Data)}")

    val day11Data = readData(day = 11)
    val octopusGrid = parseOctopusGrid(day11Data)
    println("Day 11 Part 1: ${simulateOctopusGrid(octopusGrid, numGenerations = 100).numFlashes}")
    println("Day 11 Part 2: ${findFirstSynchronizedGeneration(octopusGrid)}")

    val day12Data = readData(day = 12)
    val caveSystem = parseCaveSystem(day12Data)
    println("Day 12 Part 1: ${countAllPaths(caveSystem, allowDoubleEntryToOneSmallCave = false)}")
    println("Day 12 Part 2: ${countAllPaths(caveSystem, allowDoubleEntryToOneSmallCave = true)}")

    val day13Data = readData(day = 13, removeBlankLines = false)
    val thermalCameraManual = parseThermalCameraManual(day13Data)
    println("Day 13 Part 1: ${fold(thermalCameraManual.coordinates, thermalCameraManual.foldInstructions[0]).size}")
    println("Day 14 Part 2:\n${plot(followFoldInstructions(thermalCameraManual))}")

    val day14Data = readData(day = 14)
    val polymerizationInstructions = parsePolymerizationInstructions(day14Data)
    println("Day 14 Part 1: ${followPolymerizationInstructions(polymerizationInstructions, numSteps = 10)}")
    println("Day 14 Part 2: ${followPolymerizationInstructions(polymerizationInstructions, numSteps = 40)}")

    val day15Data = readData(day = 15)
    val chitons = parseChitonRiskLevels(day15Data)
    println("Day 15 Part 1: ${computeLowestTotalRisk(chitons)}")
    println("Day 15 Part 2: ${computeLowestTotalRisk(tileCave(chitons))}")

    val day16Data = readData(day = 16)
    val packet = Packet.parse(hexToBinary(day16Data[0])).value
    println("Day 16 Part 1: ${sumOfVersionNumbers(packet)}")
    println("Day 16 Part 2: ${packet.expression.computeValue()}")

    val day17Data = readData(day = 17)
    val targetArea = parseTargetArea(day17Data.first())
    println("Day 17 Part 1: ${findMaximumAltitude(targetArea)}")
    println("Day 17 Part 2: ${countValidInitialVelocities(targetArea)}")

    val day18Data = readData(day = 18)
    println("Day 18 Part 1: ${day18Data.map { SnailfishNumber.parse(it) }.sum().magnitude()}")
    println("Day 18 Part 2: ${day18Data.map { SnailfishNumber.parse(it) }.maxPairwiseMagnitude()}")
}


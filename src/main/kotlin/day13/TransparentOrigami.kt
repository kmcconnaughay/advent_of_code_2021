package day13

import org.typemeta.funcj.data.Chr
import org.typemeta.funcj.parser.Input
import org.typemeta.funcj.parser.Parser
import org.typemeta.funcj.parser.Text.*

data class Coordinate(val x: Int, val y: Int)

enum class FoldDirection {
    LEFT,
    UP
}

data class FoldInstruction(val foldDirection: FoldDirection, val axis: Int)

data class ThermalCameraManual(val coordinates: Set<Coordinate>, val foldInstructions: List<FoldInstruction>)

fun plot(coordinates: Set<Coordinate>): String {
    val rightBoundary = coordinates.maxOf { it.x }
    val bottomBoundary = coordinates.maxOf { it.y }
    val grid = StringBuilder()

    for (y in 0..bottomBoundary) {
        for (x in 0..rightBoundary) {
            if (coordinates.contains(Coordinate(x = x, y = y))) {
                grid.append('#')
            } else {
                grid.append(' ')
            }
        }
        grid.append('\n')
    }

    return grid.toString()
}

fun followFoldInstructions(thermalCameraManual: ThermalCameraManual): Set<Coordinate> {
    var coordinates = thermalCameraManual.coordinates
    for (fold in thermalCameraManual.foldInstructions) {
        coordinates = fold(coordinates, fold)
    }
    return coordinates
}

fun fold(coordinates: Set<Coordinate>, foldInstruction: FoldInstruction): Set<Coordinate> {
    return when (foldInstruction.foldDirection) {
        FoldDirection.LEFT -> foldLeft(coordinates, foldInstruction.axis)
        FoldDirection.UP -> foldUp(coordinates, foldInstruction.axis)
    }
}

private fun foldLeft(
    coordinates: Set<Coordinate>,
    axis: Int
) = coordinates.map {
    val newX = if (it.x > axis) {
        2 * axis - it.x
    } else {
        it.x
    }
    it.copy(x = newX)
}.toSet()

private fun foldUp(
    coordinates: Set<Coordinate>,
    axis: Int
) = coordinates.map {
    val newY = if (it.y > axis) {
        2 * axis - it.y
    } else {
        it.y
    }
    it.copy(y = newY)
}.toSet()

fun parseThermalCameraManual(input: List<String>): ThermalCameraManual {
    val blankLine = input.indexOf("")
    return ThermalCameraManual(
        parseCoordinates(input.subList(0, blankLine)),
        parseFolds(input.subList(blankLine + 1, input.size))
    )
}

fun parseCoordinates(input: List<String>) = input.map { coordinateParser.parse(Input.of(it)).orThrow }.toSet()

val coordinateParser: Parser<Chr, Coordinate> = uintr.andL(chr(',')).and(uintr).map { x, y -> Coordinate(x, y) }

fun parseFolds(input: List<String>) = input.map { foldInstructionParser.parse(Input.of(it)).orThrow }

val foldDirectionParser: Parser<Chr, FoldDirection> =
    chr('x').map { FoldDirection.LEFT }.or(chr('y').map { FoldDirection.UP })

val foldInstructionParser: Parser<Chr, FoldInstruction> =
    string("fold along ").andR(foldDirectionParser).andL(chr('=')).and(uintr)
        .map { foldDirection, axis -> FoldInstruction(foldDirection, axis) }

package day17

import org.typemeta.funcj.data.Chr
import org.typemeta.funcj.parser.Input
import org.typemeta.funcj.parser.Parser
import org.typemeta.funcj.parser.Text.intr
import org.typemeta.funcj.parser.Text.string
import triangularNumber
import kotlin.math.abs
import kotlin.math.max

data class Position(val x: Int, val y: Int)

data class Velocity(val x: Int, val y: Int)

data class TargetArea(val xRange: IntRange, val yRange: IntRange)

sealed interface ShotResult

data class Hit(val maximumAltitude: Int) : ShotResult

object Miss : ShotResult

private val rangeParser: Parser<Chr, IntRange> =
    intr.andL(string("..")).and(intr).map { start, endInclusive -> IntRange(start, endInclusive) }

private val targetAreaParser: Parser<Chr, TargetArea> =
    string("target area: x=").andR(rangeParser).andL(string(", y=")).and(rangeParser)
        .map { xRange, yRange -> TargetArea(xRange, yRange) }

fun parseTargetArea(input: String): TargetArea =
    targetAreaParser.parse(Input.of(input)).orThrow

fun countValidInitialVelocities(targetArea: TargetArea): Int {
    require(targetArea.yRange.first < 0 && targetArea.yRange.last < 0) { "The target area is not entirely below the horizontal" }
    require(targetArea.yRange.first < targetArea.yRange.last) { "The minimum y value must be given second" }
    val possibleXVelocities = 1..(targetArea.xRange.maxOrNull()!! + 1)
    val possibleYVelocities =
        targetArea.yRange.minOrNull()!!..(abs(targetArea.yRange.minOrNull()!!) + 1)

    val validInitialVelocities = mutableListOf<Velocity>()
    for (xVelocity in possibleXVelocities) {
        for (yVelocity in possibleYVelocities) {
            val initialVelocity = Velocity(x = xVelocity, y = yVelocity)
            val shotResult =
                simulateShot(initialVelocity = initialVelocity, targetArea = targetArea)
            if (shotResult is Hit) {
                validInitialVelocities.add(initialVelocity)
            }
        }
    }

    return validInitialVelocities.size
}

fun findMaximumAltitude(targetArea: TargetArea): Int {
    require(targetArea.yRange.first < 0 && targetArea.yRange.last < 0) { "The target area is not entirely below the horizontal" }
    require(targetArea.yRange.first < targetArea.yRange.last) { "The minimum y value must be given second" }
    val possibleXVelocities = 1..targetArea.xRange.last
    val possibleYVelocities = targetArea.yRange.minOrNull()!!..(abs(targetArea.yRange.minOrNull()!!) + 1)

    var maximumAltitude = 0
    for (xVelocity in possibleXVelocities) {
        for (yVelocity in possibleYVelocities) {
            val shotResult =
                simulateShot(initialVelocity = Velocity(x = xVelocity, y = yVelocity), targetArea = targetArea)
            if (shotResult is Hit) {
                maximumAltitude = max(shotResult.maximumAltitude, maximumAltitude)
            }
        }
    }

    return maximumAltitude
}

fun simulateShot(
    initialPosition: Position = Position(x = 0, y = 0),
    initialVelocity: Velocity,
    targetArea: TargetArea
): ShotResult {
    var position = initialPosition
    var velocity = initialVelocity
    while (position.x <= targetArea.xRange.maxOrNull()!! && position.y >= targetArea.yRange.minOrNull()!!) {
        position = Position(x = position.x + velocity.x, y = position.y + velocity.y)
        velocity = Velocity(x = max(0, velocity.x - 1), y = velocity.y - 1)

        if (velocity.x == 0 && position.x < targetArea.xRange.minOrNull()!!) {
            return Miss
        }

        if (targetArea.xRange.contains(position.x) && targetArea.yRange.contains(position.y)) {
            return Hit(triangularNumber(max(0, initialVelocity.y)))
        }
    }

    return Miss
}

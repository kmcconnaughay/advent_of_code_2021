package day05

import org.typemeta.funcj.data.Chr
import org.typemeta.funcj.parser.Input
import org.typemeta.funcj.parser.Parser
import org.typemeta.funcj.parser.Result
import org.typemeta.funcj.parser.Text.chr
import org.typemeta.funcj.parser.Text.uintr
import org.typemeta.funcj.parser.Text.string
import kotlin.math.max
import kotlin.math.min

data class CartesianPoint(val x: Int, val y: Int)

data class LineSegment(val start: CartesianPoint, val end: CartesianPoint) {

    fun isHorizontal(): Boolean {
        return start.y == end.y
    }

    fun isVertical(): Boolean {
        return start.x == end.x
    }

    fun coveredPoints(): Set<CartesianPoint> {
        if (isVertical()) {
            return (min(start.y, end.y)..max(start.y, end.y)).map { y -> CartesianPoint(x = start.x, y = y) }.toSet()
        }

        if (isHorizontal()) {
            return (min(start.x, end.x)..max(start.x, end.x)).map { x -> CartesianPoint(x = x, y = start.y) }.toSet()
        }

        val xRange = if (start.x < end.x) {
            start.x..end.x
        } else {
            start.x downTo end.x
        }
        val yRange = if (start.y < end.y) {
            start.y..end.y
        } else {
            start.y downTo end.y
        }
        // Take advantage of the fact that the given line segments are only ever at 45 degrees, which means that the
        // lengths of xRange and yRange will always be identical.
        return xRange.zip(yRange).map { (x, y) -> CartesianPoint(x = x, y = y) }.toSet()
    }
}

fun countIntersections(
    lineSegments: List<LineSegment>,
    onlyConsiderVerticalAndHorizontalLineSegments: Boolean = false
): Int {
    val intersectionHistogram: MutableMap<CartesianPoint, Int> = HashMap()

    val consideredLineSegments = if (onlyConsiderVerticalAndHorizontalLineSegments) {
        lineSegments.filter { lineSegment -> lineSegment.isHorizontal() || lineSegment.isVertical() }
    } else {
        lineSegments
    }

    for (lineSegment in consideredLineSegments) {
        lineSegment.coveredPoints().forEach { point ->
            intersectionHistogram[point] = intersectionHistogram.getOrDefault(point, 0) + 1
        }
    }

    return intersectionHistogram.filterValues { count -> count > 1 }.count()
}

fun parseLineSegment(lineSegment: String): LineSegment {
    val result: Result<Chr, LineSegment> = lineSegmentParser.parse(Input.of(lineSegment))
    return result.orThrow
}

private val cartesianPointParser: Parser<Chr, CartesianPoint> =
    uintr.andL(chr(',')).and(uintr).map { x, y -> CartesianPoint(x = x, y = y) }

/**
 * A [Parser] that parses the following grammar:
 *
 * lineSegment ::= <cartesianPoint>" -> "<cartesianPoint>
 * cartesianPoint ::= <number>","<number>
 * number ::= "0" | <nonZeroNumber>
 * nonZeroNumber ::= <nonZeroDigit><digits>
 * digits ::= <digit> | <digit><digits>
 * digit ::= "0" | <nonZeroDigit>
 * nonZeroDigit :: = "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
 */
private val lineSegmentParser: Parser<Chr, LineSegment> =
    cartesianPointParser
        .andL(string(" -> "))
        .and(cartesianPointParser)
        .map { start, end -> LineSegment(start = start, end = end) }

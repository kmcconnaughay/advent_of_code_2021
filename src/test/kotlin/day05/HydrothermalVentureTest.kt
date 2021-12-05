package day05

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class HydrothermalVentureTest {

    @Test
    fun parseLineSegment_valid_returnsLineSegment() {
        assertEquals(
            LineSegment(CartesianPoint(x = 0, y = 1), CartesianPoint(x = 4, y = 0)),
            parseLineSegment("0,1 -> 4,0")
        )
        assertEquals(
            LineSegment(CartesianPoint(x = 451, y = 12), CartesianPoint(x = 3, y = 90)),
            parseLineSegment("451,12 -> 3,90")
        )
    }

    @Test
    fun parseLineSegment_invalid_throwsRuntimeException() {
        assertThrows<RuntimeException> { parseLineSegment("1,2-> 3,4") }
        assertThrows<RuntimeException> { parseLineSegment("1, 2 -> 3,4") }
        assertThrows<RuntimeException> { parseLineSegment("1,2 -> 3 ,4") }
        assertThrows<RuntimeException> { parseLineSegment("1,2 ->3,4") }
        assertThrows<RuntimeException> { parseLineSegment("12 -> 3,4") }
        assertThrows<RuntimeException> { parseLineSegment("1,2 -> 34") }
        assertThrows<RuntimeException> { parseLineSegment("1,2 3,4") }
    }

    @Test
    fun countIntersections_onlyConsideringHorizontalAndVerticalLineSegments_returnsNumberOfIntersections() {
        val input = listOf(
            "0,9 -> 5,9",
            "8,0 -> 0,8",
            "9,4 -> 3,4",
            "2,2 -> 2,1",
            "7,0 -> 7,4",
            "6,4 -> 2,0",
            "0,9 -> 2,9",
            "3,4 -> 1,4",
            "0,0 -> 8,8",
            "5,5 -> 8,2",
        )
        val lineSegments = input.map(::parseLineSegment)

        val numIntersections = countIntersections(lineSegments, onlyConsiderVerticalAndHorizontalLineSegments = true)

        assertEquals(5, numIntersections)
    }

    @Test
    fun countIntersections_consideringEverything_returnsNumberOfIntersections() {
        val input = listOf(
            "0,9 -> 5,9",
            "8,0 -> 0,8",
            "9,4 -> 3,4",
            "2,2 -> 2,1",
            "7,0 -> 7,4",
            "6,4 -> 2,0",
            "0,9 -> 2,9",
            "3,4 -> 1,4",
            "0,0 -> 8,8",
            "5,5 -> 8,2",
        )
        val lineSegments = input.map(::parseLineSegment)

        val numIntersections = countIntersections(lineSegments)

        assertEquals(12, numIntersections)
    }
}
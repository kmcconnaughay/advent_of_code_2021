package day09

import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class SmokeBasinTest {

    @Test
    fun computeRiskLevels_findsLowPoints() {
        val heightMap = parseHeightMap(
            listOf(
                "2199943210",
                "3987894921",
                "9856789892",
                "8767896789",
                "9899965678"
            )
        )

        val riskLevels = computeRiskLevels(heightMap)

        assertEquals(
            mk.ndarray(
                mk[
                        mk[0, 2, 0, 0, 0, 0, 0, 0, 0, 1],
                        mk[0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                        mk[0, 0, 6, 0, 0, 0, 0, 0, 0, 0],
                        mk[0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                        mk[0, 0, 0, 0, 0, 0, 6, 0, 0, 0],
                ]
            ),
            riskLevels
        )
    }

    @Test
    fun findBasins_returnsSetsOfCoordinatesInEachBasin() {
        val heightMap = parseHeightMap(
            listOf(
                "2199943210",
                "3987894921",
                "9856789892",
                "8767896789",
                "9899965678"
            )
        )

        val basins = findBasins(heightMap)

        val firstBasin = Basin(
            setOf(
                Coordinate(row = 0, column = 0),
                Coordinate(row = 0, column = 1),
                Coordinate(row = 1, column = 0),
            )
        )
        val secondBasin = Basin(
            setOf(
                Coordinate(row = 0, column = 5),
                Coordinate(row = 0, column = 6),
                Coordinate(row = 0, column = 7),
                Coordinate(row = 0, column = 8),
                Coordinate(row = 0, column = 9),
                Coordinate(row = 1, column = 6),
                Coordinate(row = 1, column = 6),
                Coordinate(row = 1, column = 8),
                Coordinate(row = 1, column = 9),
                Coordinate(row = 2, column = 9),
            )
        )
        val thirdBasin = Basin(
            setOf(
                Coordinate(row = 1, column = 2),
                Coordinate(row = 1, column = 3),
                Coordinate(row = 1, column = 4),
                Coordinate(row = 2, column = 1),
                Coordinate(row = 2, column = 2),
                Coordinate(row = 2, column = 3),
                Coordinate(row = 2, column = 4),
                Coordinate(row = 2, column = 5),
                Coordinate(row = 3, column = 0),
                Coordinate(row = 3, column = 1),
                Coordinate(row = 3, column = 2),
                Coordinate(row = 3, column = 3),
                Coordinate(row = 3, column = 4),
                Coordinate(row = 4, column = 1),
            )
        )
        val fourthBasin = Basin(
            setOf(
                Coordinate(row = 2, column = 7),
                Coordinate(row = 3, column = 6),
                Coordinate(row = 3, column = 7),
                Coordinate(row = 3, column = 8),
                Coordinate(row = 4, column = 5),
                Coordinate(row = 4, column = 6),
                Coordinate(row = 4, column = 7),
                Coordinate(row = 4, column = 8),
                Coordinate(row = 4, column = 9),
            )
        )
        assertEquals(setOf(firstBasin, secondBasin, thirdBasin, fourthBasin), basins)
    }
}
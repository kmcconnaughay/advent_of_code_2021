package day13

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class TransparentOrigamiTest {

    @Test
    fun foobar() {
        val thermalCameraManual = parseThermalCameraManual(
            listOf(
                "6,10",
                "0,14",
                "9,10",
                "0,3",
                "10,4",
                "4,11",
                "6,0",
                "6,12",
                "4,1",
                "0,13",
                "10,12",
                "3,4",
                "3,0",
                "8,4",
                "1,10",
                "2,14",
                "8,10",
                "9,0",
                "",
                "fold along y=7",
                "fold along x=5",
            )
        )

        val coordinates = followFoldInstructions(thermalCameraManual)

        assertEquals(
            setOf(
                Coordinate(x = 0, y = 0),
                Coordinate(x = 1, y = 0),
                Coordinate(x = 2, y = 0),
                Coordinate(x = 3, y = 0),
                Coordinate(x = 4, y = 0),
                Coordinate(x = 0, y = 1),
                Coordinate(x = 4, y = 1),
                Coordinate(x = 0, y = 2),
                Coordinate(x = 4, y = 2),
                Coordinate(x = 0, y = 3),
                Coordinate(x = 4, y = 3),
                Coordinate(x = 0, y = 4),
                Coordinate(x = 1, y = 4),
                Coordinate(x = 2, y = 4),
                Coordinate(x = 3, y = 4),
                Coordinate(x = 4, y = 4),
            ),
            coordinates
        )
    }
}
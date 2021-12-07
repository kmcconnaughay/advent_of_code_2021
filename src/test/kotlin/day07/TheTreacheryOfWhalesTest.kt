package day07

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class TheTreacheryOfWhalesTest {

    @Test
    fun alignPositionsWithTriangularFuelConsumption_minimizesFuelConsumption() {
        val positionHistogram = listOf(1, 2, 3, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1)

        val consumedFuel = alignPositionsWithTriangularFuelConsumption(positionHistogram)

        assertEquals(168, consumedFuel)
    }

    @Test
    fun alignPositionsWithLinearFuelConsumption_minimizesFuelConsumption() {
        val positionHistogram = listOf(1, 2, 3, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1)

        val consumedFuel = alignPositionsWithLinearFuelConsumption(positionHistogram)

        assertEquals(37, consumedFuel)
    }

    @Test
    fun createPositionHistogram_countsCrabsAtEachPosition() {
        val positions = listOf("16,1,2,0,4,2,7,1,2,14")

        val positionHistogram = createPositionHistogram(positions)

        assertEquals(listOf(1, 2, 3, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1), positionHistogram)
    }
}
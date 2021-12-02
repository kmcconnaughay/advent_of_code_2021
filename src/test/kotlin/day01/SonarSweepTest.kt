package day01

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SonarSweepTest {

    @Test
    fun numDepthMeasurementIncreases_emptyList_returnsZero() {
        assertEquals(0, numDepthMeasurementIncreases(listOf()))
    }

    @Test
    fun numDepthMeasurementIncreases_onlyDecreases_returnsZero() {
        val depthMeasurements = listOf("200", "150", "100", "50")

        val numIncreases = numDepthMeasurementIncreases(depthMeasurements)

        assertEquals(0, numIncreases)
    }

    @Test
    fun numDepthMeasurementIncreases_someIncreases_countsIncreases() {
        val depthMeasurements = listOf("199", "200", "208", "210", "200", "207", "240", "269", "269", "260", "263")

        val numIncreases = numDepthMeasurementIncreases(depthMeasurements)

        assertEquals(7, numIncreases)
    }

    @Test
    fun numAveragedDepthMeasurementIncreases_emptyList_returnsZero() {
        assertEquals(0, numAveragedDepthMeasurementIncreases(listOf()))
    }

    @Test
    fun numAveragedDepthMeasurementIncreases_fewerThanFourMeasurements_returnsZero() {
        assertEquals(0, numAveragedDepthMeasurementIncreases(listOf("300", "400", "500")))
    }

    @Test
    fun numAveragedDepthMeasurementIncreases_noIncreases_returnsZero() {
        val depthMeasurements = listOf("500", "400", "300", "200", "100")

        val numIncreases = numAveragedDepthMeasurementIncreases(depthMeasurements)

        assertEquals(0, numIncreases)
    }

    @Test
    fun numAveragedDepthMeasurementIncreases_someIncreases_countsIncreases() {
        val depthMeasurements = listOf("199", "200", "208", "210", "200", "207", "240", "269", "260", "263")

        val numIncreases = numAveragedDepthMeasurementIncreases(depthMeasurements)

        assertEquals(5, numIncreases)
    }
}
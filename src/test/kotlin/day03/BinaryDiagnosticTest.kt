package day03

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class BinaryDiagnosticTest {

    @Test
    fun computePowerConsumption_noDiagnostics_returnsZeros() {
        assertEquals(
            PowerConsumption(gammaRate = 0, epsilonRate = 0, powerConsumption = 0),
            computePowerConsumption(listOf())
        )
    }

    @Test
    fun computePowerConsumption_returnsConsumption() {
        val diagnosticReport = listOf(
            "00100",
            "11110",
            "10110",
            "10111",
            "10101",
            "01111",
            "00111",
            "11100",
            "10000",
            "11001",
            "00010",
            "01010"
        )

        val powerConsumption = computePowerConsumption(diagnosticReport)

        assertEquals(PowerConsumption(gammaRate = 22, epsilonRate = 9, powerConsumption = 198), powerConsumption)
    }

    @Test
    fun computeLifeSupportRating_noDiagnostics_returnsZeroes() {
        assertEquals(
            LifeSupportRating(oxygenGeneratorRating = 0, co2ScrubberRating = 0, lifeSupportRating = 0),
            computeLifeSupportRating(listOf())
        )
    }

    @Test
    fun computeLifeSupportRating_computesRating() {
        val diagnosticReport = listOf(
            "00100",
            "11110",
            "10110",
            "10111",
            "10101",
            "01111",
            "00111",
            "11100",
            "10000",
            "11001",
            "00010",
            "01010"
        )

        val lifeSupportRating = computeLifeSupportRating(diagnosticReport)

        assertEquals(
            LifeSupportRating(oxygenGeneratorRating = 23, co2ScrubberRating = 10, lifeSupportRating = 230),
            lifeSupportRating
        )
    }
}
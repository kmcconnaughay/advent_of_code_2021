package day21

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class DiracDiceTest {

    @Test
    fun moduloWithOffset() {
        assertEquals(1, moduloWithOffset(dividend = 1, divisor = 5, offset = 1))
        assertEquals(2, moduloWithOffset(dividend = 2, divisor = 5, offset = 1))
        assertEquals(3, moduloWithOffset(dividend = 3, divisor = 5, offset = 1))
        assertEquals(4, moduloWithOffset(dividend = 4, divisor = 5, offset = 1))
        assertEquals(5, moduloWithOffset(dividend = 5, divisor = 5, offset = 1))
        assertEquals(1, moduloWithOffset(dividend = 6, divisor = 5, offset = 1))
    }

    @Test
    fun playPracticeGame() {
        val startingPositions =
            StartingPositions.parse(listOf("Player 1 starting position: 4", "Player 2 starting position: 8"))

        val result = playPracticeGame(startingPositions)

        assertEquals(739785, result)
    }

    @Test
    fun playDiracGame() {
        val startingPositions =
            StartingPositions.parse(listOf("Player 1 starting position: 4", "Player 2 starting position: 8"))

        val result = playDiracGame(startingPositions)

        assertEquals(444356092776315, result)
    }
}
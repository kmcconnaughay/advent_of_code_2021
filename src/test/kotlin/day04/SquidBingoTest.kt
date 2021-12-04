package day04

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class SquidBingoTest {

    @Test
    fun unmarkedNumbers_noMarksInBoard_returnsAllNumbers() {
        val input = listOf(
            " 3 15  0  2 22",
            " 9 18 13 17  5",
            "19  8  7 25 23",
            "20 11 10 24  4",
            "14 21 16 12  6"
        )
        val bingoBoard = BingoBoard.parse(input)

        bingoBoard.mark(1000)
        val unmarkedNumbers = bingoBoard.unmarkedNumbers()

        assertEquals(
            listOf(3, 15, 0, 2, 22, 9, 18, 13, 17, 5, 19, 8, 7, 25, 23, 20, 11, 10, 24, 4, 14, 21, 16, 12, 6),
            unmarkedNumbers
        )
    }

    @Test
    fun unmarkedNumbers_marksInBoard_omitsMarkedNumbers() {
        val input = listOf(" 3 15  0  2 22")
        val bingoBoard = BingoBoard.parse(input)

        bingoBoard.mark(15)
        val unmarkedNumbers = bingoBoard.unmarkedNumbers()

        assertEquals(listOf(3, 0, 2, 22), unmarkedNumbers)
    }

    @Test
    fun getBingoNumber_indicesOutsideBoard_throwsIllegalArgumentException() {
        val input = listOf(
            " 3 15  0  2 22",
            " 9 18 13 17  5",
            "19  8  7 25 23",
            "20 11 10 24  4",
        )
        val bingoBoard = BingoBoard.parse(input)

        assertThrows<IllegalArgumentException> { bingoBoard.getBingoNumber(column = 5, row = 2) }
        assertThrows<IllegalArgumentException> { bingoBoard.getBingoNumber(column = -1, row = 2) }
        assertThrows<IllegalArgumentException> { bingoBoard.getBingoNumber(column = 3, row = 4) }
        assertThrows<IllegalArgumentException> { bingoBoard.getBingoNumber(column = 3, row = -1) }
    }

    @Test
    fun getBingoNumber_indexesIntoBoard() {
        val input = listOf(
            " 3 15  0  2 22",
            " 9 18 13 17  5",
            "19  8  7 25 23",
            "20 11 10 24  4",
        )
        val bingoBoard = BingoBoard.parse(input)

        assertEquals(3, bingoBoard.getBingoNumber(column = 0, row = 0).value)
        assertEquals(4, bingoBoard.getBingoNumber(column = 4, row = 3).value)
        assertEquals(8, bingoBoard.getBingoNumber(column = 1, row = 2).value)
        assertEquals(17, bingoBoard.getBingoNumber(column = 3, row = 1).value)
    }

    @Test
    fun hasWon_incompleteColumnAndRowAndDiagonals_returnsFalse() {
        // Given a random board
        val input = listOf(
            " 3 15  0  2 22",
            " 9 18 13 17  5",
            "19  8  7 25 23",
            "20 11 10 24  4",
            "14 21 16 12  6"
        )
        val bingoBoard = BingoBoard.parse(input)

        // When  a column is not completely marked
        bingoBoard.mark(15)
        bingoBoard.mark(18)
        bingoBoard.mark(8)
        bingoBoard.mark(11)

        // And a row is not completely marked
        bingoBoard.mark(20)
        bingoBoard.mark(11)
        bingoBoard.mark(24)
        bingoBoard.mark(4)

        // And hasWon is invoked
        val hasWon = bingoBoard.hasWon()

        // Then the returned value is false
        assertFalse(hasWon)
    }

    @Test
    fun hasWon_markedColumn_returnsTrue() {
        val input = listOf(
            " 3 15  0  2 22",
            " 9 18 13 17  5",
            "19  8  7 25 23",
            "20 11 10 24  4",
            "14 21 16 12  6"
        )
        val bingoBoard = BingoBoard.parse(input)

        bingoBoard.mark(22)
        bingoBoard.mark(5)
        bingoBoard.mark(23)
        bingoBoard.mark(4)
        bingoBoard.mark(6)
        val hasWon = bingoBoard.hasWon()

        assertTrue(hasWon)
    }

    @Test
    fun hasWon_markedRow_returnsTrue() {
        val input = listOf(
            " 3 15  0  2 22",
            " 9 18 13 17  5",
            "19  8  7 25 23",
            "20 11 10 24  4",
            "14 21 16 12  6"
        )
        val bingoBoard = BingoBoard.parse(input)

        bingoBoard.mark(20)
        bingoBoard.mark(11)
        bingoBoard.mark(10)
        bingoBoard.mark(24)
        bingoBoard.mark(4)
        val hasWon = bingoBoard.hasWon()

        assertTrue(hasWon)
    }

    @Test
    fun runToFirstWinner_returnsBingoScore() {
        val input = listOf(
            // Drawn numbers
            "7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1",
            // First board
            "22 13 17 11  0",
            " 8  2 23  4 24",
            "21  9 14 16  7",
            " 6 10  3 18  5",
            " 1 12 20 15 19",
            // Second board
            " 3 15  0  2 22",
            " 9 18 13 17  5",
            "19  8  7 25 23",
            "20 11 10 24  4",
            "14 21 16 12  6",
            // Third board
            "14 21 17 24  4",
            "10 16 15  9 19",
            "18  8 23 26 20",
            "22 11 13  6  5",
            " 2  0 12  3  7",
        )
        val bingoGame = BingoGame.parse(input)

        val answer = bingoGame.runToFirstWinner()

        assertEquals(4512, answer)
    }

    @Test
    fun runToLastWinner_returnsBingoScore() {
        val input = listOf(
            // Drawn numbers
            "7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1",
            // First board
            "22 13 17 11  0",
            " 8  2 23  4 24",
            "21  9 14 16  7",
            " 6 10  3 18  5",
            " 1 12 20 15 19",
            // Second board
            " 3 15  0  2 22",
            " 9 18 13 17  5",
            "19  8  7 25 23",
            "20 11 10 24  4",
            "14 21 16 12  6",
            // Third board
            "14 21 17 24  4",
            "10 16 15  9 19",
            "18  8 23 26 20",
            "22 11 13  6  5",
            " 2  0 12  3  7",
        )
        val bingoGame = BingoGame.parse(input)

        val answer = bingoGame.runToLastWinner()

        assertEquals(1924, answer)
    }
}
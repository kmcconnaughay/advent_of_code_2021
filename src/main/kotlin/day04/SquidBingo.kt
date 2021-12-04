package day04

import java.util.regex.Pattern

data class BingoGame(val drawnNumbers: List<Int>, val bingoBoards: List<BingoBoard>) {

    fun runToFirstWinner(): Int {
        for (number in drawnNumbers) {
            bingoBoards.forEach { bingoBoard -> bingoBoard.mark(number) }
            val winningBoard = bingoBoards.filter(BingoBoard::hasWon).firstOrNull()
            if (winningBoard != null) {
                return winningBoard.unmarkedNumbers().sum() * number
            }
        }

        return 0
    }

    fun runToLastWinner(): Int {
        var remainingBoards = bingoBoards
        for (number in drawnNumbers) {
            remainingBoards.forEach { bingoBoard -> bingoBoard.mark(number) }

            if (remainingBoards.size == 1 && remainingBoards[0].hasWon()) {
                return remainingBoards[0].unmarkedNumbers().sum() * number
            } else {
                remainingBoards = remainingBoards.filter { bingoBoard -> !bingoBoard.hasWon() }
            }
        }

        return 0
    }

    companion object {
        fun parse(input: List<String>): BingoGame {
            val drawnNumbers = input[0].split(",").map(Integer::parseInt)
            val bingoBoards = input.subList(1, input.size).chunked(5).map(BingoBoard::parse)
            return BingoGame(drawnNumbers, bingoBoards)
        }
    }
}

data class BingoNumber(val value: Int, var marked: Boolean)

class BingoBoard private constructor(
    private val numColumns: Int,
    private val numRows: Int,
    private val board: List<BingoNumber>
) {

    fun hasWon(): Boolean {
        return hasWinningColumn() || hasWinningRow()
    }

    private fun hasWinningColumn(): Boolean {
        for (column in 0 until numColumns) {
            var isWinningColumn = true
            for (row in 0 until numRows) {
                isWinningColumn = isWinningColumn && getBingoNumber(column = column, row = row).marked
            }

            if (isWinningColumn) {
                return true
            }
        }

        return false
    }

    private fun hasWinningRow(): Boolean {
        for (row in 0 until numRows) {
            var isWinningRow = true
            for (column in 0 until numColumns) {
                isWinningRow = isWinningRow && getBingoNumber(column = column, row = row).marked
            }

            if (isWinningRow) {
                return true
            }
        }

        return false
    }

    fun getBingoNumber(column: Int, row: Int): BingoNumber {
        if (column < 0 || column > numColumns - 1) {
            throw IllegalArgumentException(
                "The column parameter must be in the range [0, $numColumns), but was $column"
            )
        }
        if (row < 0 || row > numRows - 1) {
            throw IllegalArgumentException("The row parameter must be in the range [0, $numRows), but was $row")
        }
        return board[row * numColumns + column]
    }

    fun mark(number: Int) {
        board.filter { bingoNumber -> bingoNumber.value == number }.forEach { bingoNumber -> bingoNumber.marked = true }
    }

    fun unmarkedNumbers(): List<Int> {
        return board.filter { bingoNumber -> !bingoNumber.marked }.map(BingoNumber::value)
    }

    companion object {
        private val SPACES = Pattern.compile(" +")

        fun parse(input: List<String>): BingoBoard {
            val numRows = input.size
            val board =
                input.flatMap { row -> row.trim().split(SPACES) }
                    .map(Integer::parseInt)
                    .map { number -> BingoNumber(value = number, marked = false) }
            val numColumns = board.size / numRows
            return BingoBoard(numColumns = numColumns, numRows = numRows, board = board)
        }
    }
}
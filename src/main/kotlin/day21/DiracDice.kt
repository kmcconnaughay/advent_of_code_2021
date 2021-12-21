package day21

import org.typemeta.funcj.data.Chr
import org.typemeta.funcj.parser.Input
import org.typemeta.funcj.parser.Parser
import org.typemeta.funcj.parser.Text.string
import org.typemeta.funcj.parser.Text.uintr

enum class Player {
    ONE, TWO;

    companion object {
        fun nextPlayer(player: Player): Player = when (player) {
            ONE -> TWO
            TWO -> ONE
        }
    }
}

data class StartingPositions(val playerOnePosition: Int, val playerTwoPosition: Int) {
    companion object {
        private val startingPositionParser: Parser<Chr, Int> =
            string("Player ").andR(uintr.andR(string(" starting position: ").andR(uintr)))

        fun parse(input: List<String>): StartingPositions = StartingPositions(
            playerOnePosition = startingPositionParser.parse(Input.of(input.first())).orThrow,
            playerTwoPosition = startingPositionParser.parse(Input.of(input.last())).orThrow
        )
    }
}

fun moduloWithOffset(dividend: Int, divisor: Int, offset: Int): Int =
    dividend - divisor * ((dividend - offset) / divisor)

interface Die {
    fun rollThree(): Map<Int, Int>
}

class DeterministicDie : Die {
    private val deterministicDie: Iterator<Int> =
        generateSequence(1) { moduloWithOffset(dividend = it + 1, divisor = 100, offset = 1) }.iterator()

    override fun rollThree(): Map<Int, Int> {
        return mapOf(deterministicDie.take(3).sum() to 1)
    }
}

class DiracDie : Die {
    private val faces = 1..3
    private val rollHistogram =
        faces.flatMap { a -> faces.flatMap { b -> faces.map { c -> a + b + c } } }.groupingBy { it }.eachCount()

    override fun rollThree(): Map<Int, Int> {
        return rollHistogram
    }

}

fun playPracticeGame(startingPositions: StartingPositions): Long {
    val die: Die = DeterministicDie()
    val startingGameState = DiracDiceState(
        round = 0,
        playerOneScore = 0,
        playerOnePosition = startingPositions.playerOnePosition,
        playerTwoScore = 0,
        playerTwoPosition = startingPositions.playerTwoPosition
    )
    var gameStateHistogram = hashMapOf(startingGameState to 1L)
    var player = Player.ONE

    while (gameStateHistogram.isNotEmpty()) {
        val nextGameStateHistogram = hashMapOf<DiracDiceState, Long>()
        for ((gameState, gameCount) in gameStateHistogram) {
            for ((roll, rollCount) in die.rollThree()) {
                val nextGameState = gameState.move(player, roll)
                val nextGameCount = gameCount * rollCount

                if (nextGameState.playerScore(player) >= 1000) {
                    return nextGameState.playerScore(Player.nextPlayer(player)) * nextGameState.round * 3
                } else {
                    nextGameStateHistogram[nextGameState] = (nextGameStateHistogram[nextGameState] ?: 0) + nextGameCount
                }
            }
        }

        player = Player.nextPlayer(player)
        gameStateHistogram = nextGameStateHistogram
    }

    throw IllegalStateException()
}

data class DiracDiceState(
    val round: Int,
    val playerOneScore: Long,
    val playerOnePosition: Int,
    val playerTwoScore: Long,
    val playerTwoPosition: Int
) {
    fun move(player: Player, roll: Int): DiracDiceState {
        val newPlayerPosition = moduloWithOffset(dividend = playerPosition(player) + roll, divisor = 10, offset = 1)
        val newPlayerScore = playerScore(player) + newPlayerPosition
        return copyWithNewPlayerData(player, newPlayerScore, newPlayerPosition, newRound = round + 1)
    }

    private fun playerPosition(player: Player): Int = when (player) {
        Player.ONE -> playerOnePosition
        Player.TWO -> playerTwoPosition
    }

    fun playerScore(player: Player): Long = when (player) {
        Player.ONE -> playerOneScore
        Player.TWO -> playerTwoScore
    }

    private fun copyWithNewPlayerData(
        player: Player, newPlayerScore: Long, newPlayerPosition: Int, newRound: Int
    ): DiracDiceState = when (player) {
        Player.ONE -> copy(round = newRound, playerOneScore = newPlayerScore, playerOnePosition = newPlayerPosition)
        Player.TWO -> copy(round = newRound, playerTwoScore = newPlayerScore, playerTwoPosition = newPlayerPosition)
    }
}

fun playDiracGame(startingPositions: StartingPositions): Long {
    val die: Die = DiracDie()
    val startingGameState = DiracDiceState(
        round = 1,
        playerOneScore = 0,
        playerOnePosition = startingPositions.playerOnePosition,
        playerTwoScore = 0,
        playerTwoPosition = startingPositions.playerTwoPosition
    )
    var gameStateHistogram = hashMapOf(startingGameState to 1L)
    var player = Player.ONE
    val wins = hashMapOf<Player, Long>()

    while (gameStateHistogram.isNotEmpty()) {
        val nextGameStateHistogram = hashMapOf<DiracDiceState, Long>()
        for ((gameState, gameCount) in gameStateHistogram) {
            for ((roll, rollCount) in die.rollThree()) {
                val nextGameState = gameState.move(player, roll)
                val nextGameCount: Long = gameCount * rollCount

                if (nextGameState.playerScore(player) >= 21) {
                    wins[player] = (wins[player] ?: 0) + nextGameCount
                } else {
                    nextGameStateHistogram[nextGameState] = (nextGameStateHistogram[nextGameState] ?: 0) + nextGameCount
                }
            }
        }
        player = Player.nextPlayer(player)
        gameStateHistogram = nextGameStateHistogram
    }

    return wins.maxOf { (_, numWins) -> numWins }
}

fun <T> Iterator<T>.take(n: Int): List<T> {
    val result = mutableListOf<T>()
    for (i in 1..n) {
        if (hasNext()) {
            result.add(next())
        } else {
            return result
        }
    }
    return result
}

package day02

import org.typemeta.funcj.data.Chr
import org.typemeta.funcj.parser.Input
import org.typemeta.funcj.parser.Parser
import org.typemeta.funcj.parser.Result
import org.typemeta.funcj.parser.Text.*
import kotlin.math.max

data class Command(val operation: Operation, val distance: Int)

enum class Operation {
    FORWARD,
    DOWN,
    UP
}

data class Position(val horizontalPosition: Int, val depth: Int)

fun parseCommand(command: String): Command {
    val result: Result<Chr, Command> = commandParser.parse(Input.of(command))
    return result.orThrow
}

private val operationParser: Parser<Chr, Operation> =
    string("forward").or(string("up"))
        .or(string("down"))
        .map { direction -> Operation.valueOf(direction.uppercase()) }

/**
 * A [Parser] that parses the following grammar:
 *
 * command ::= <operation>" "<number>
 * operation ::= "forward" | "down" | "up"
 * number ::= <digit><number>
 * digit ::= "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
 */
private val commandParser: Parser<Chr, Command> =
    operationParser.andL(chr(' ')).and(uintr).map { direction, distance -> Command(direction, distance) }

fun runCommands(commands: List<String>): Position {
    return commands.map(::parseCommand).fold(Position(0, 0), ::applyCommand)
}

private fun applyCommand(position: Position, command: Command): Position {
    return when (command.operation) {
        Operation.FORWARD -> position.copy(horizontalPosition = position.horizontalPosition + command.distance)
        Operation.DOWN -> position.copy(depth = position.depth + command.distance)
        Operation.UP -> position.copy(depth = max(position.depth - command.distance, 0))
    }
}

data class PositionWithAim(val horizontalPosition: Int, val depth: Int, val aim: Int)

fun runCommandsWithAim(commands: List<String>): PositionWithAim {
    return commands.map(::parseCommand).fold(PositionWithAim(0, 0, 0), ::applyCommandWithAim)
}

private fun applyCommandWithAim(position: PositionWithAim, command: Command): PositionWithAim {
    return when (command.operation) {
        Operation.FORWARD -> position.copy(
            horizontalPosition = position.horizontalPosition + command.distance,
            depth = position.depth + position.aim * command.distance
        )
        Operation.DOWN -> position.copy(aim = position.aim + command.distance)
        Operation.UP -> position.copy(aim = position.aim - command.distance)
    }
}

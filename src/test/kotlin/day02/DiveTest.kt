package day02

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class DiveTest {

    @Test
    fun parseCommand_valid_returnsCommand() {
        assertEquals(Command(Operation.FORWARD, 501), parseCommand("forward 501"))
        assertEquals(Command(Operation.DOWN, 98), parseCommand("down 98"))
        assertEquals(Command(Operation.UP, 3), parseCommand("up 3"))
    }

    @Test
    fun parseCommand_invalid_throwsException() {
        assertFailsWith<RuntimeException> { parseCommand("sunward 30") }
        assertFailsWith<RuntimeException> { parseCommand("forward 01") }
        assertFailsWith<RuntimeException> { parseCommand("") }
    }

    @Test
    fun runCommands_appliesAllCommands() {
        val commands = listOf("forward 5", "down 5", "forward 8", "up 3", "down 8", "forward 2")

        val finalPosition = runCommands(commands)

        assertEquals(Position(horizontalPosition = 15, depth = 10), finalPosition)
    }

    @Test
    fun runCommandsWithAim_appliesAllCommands() {
        val commands = listOf("forward 5", "down 5", "forward 8", "up 3", "down 8", "forward 2")

        val finalPosition = runCommandsWithAim(commands)

        assertEquals(PositionWithAim(horizontalPosition = 15, depth = 60, aim = 10), finalPosition)
    }
}
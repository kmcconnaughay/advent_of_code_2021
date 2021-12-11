package day11

import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class DumboOctopusTest {

    @Test
    fun stepOctopusGrid_flashesOctopusesWithHighEnoughEnergyLevel() {
        val octopusGrid = mk.ndarray(
            mk[
                    mk[1, 1, 1, 1, 1],
                    mk[1, 9, 9, 9, 1],
                    mk[1, 9, 1, 9, 1],
                    mk[1, 9, 9, 9, 1],
                    mk[1, 1, 1, 1, 1],
            ]
        )

        val nextGrid = stepOctopusGrid(octopusGrid)

        assertEquals(
            SteppedOctopusGrid(
                grid = mk.ndarray(
                    mk[
                            mk[3, 4, 5, 4, 3],
                            mk[4, 0, 0, 0, 4],
                            mk[5, 0, 0, 0, 5],
                            mk[4, 0, 0, 0, 4],
                            mk[3, 4, 5, 4, 3],
                    ]
                ), numFlashes = 9
            ),
            nextGrid
        )
    }

    @Test
    fun simulateOctopusGrid_smallGrid_stepsThroughNGenerations() {
        val octopusGrid = mk.ndarray(
            mk[
                    mk[1, 1, 1, 1, 1],
                    mk[1, 9, 9, 9, 1],
                    mk[1, 9, 1, 9, 1],
                    mk[1, 9, 9, 9, 1],
                    mk[1, 1, 1, 1, 1],
            ]
        )

        val endGrid = simulateOctopusGrid(octopusGrid, numGenerations = 2)

        assertEquals(
            SteppedOctopusGrid(
                grid = mk.ndarray(
                    mk[
                            mk[4, 5, 6, 5, 4],
                            mk[5, 1, 1, 1, 5],
                            mk[6, 1, 1, 1, 6],
                            mk[5, 1, 1, 1, 5],
                            mk[4, 5, 6, 5, 4],
                    ]
                ), numFlashes = 9
            ),
            endGrid
        )
    }

    @Test
    fun simulateOctopusGrid_largeGrid_stepsThroughNGenerations() {
        val octopusGrid = mk.ndarray(
            mk[
                    mk[5, 4, 8, 3, 1, 4, 3, 2, 2, 3],
                    mk[2, 7, 4, 5, 8, 5, 4, 7, 1, 1],
                    mk[5, 2, 6, 4, 5, 5, 6, 1, 7, 3],
                    mk[6, 1, 4, 1, 3, 3, 6, 1, 4, 6],
                    mk[6, 3, 5, 7, 3, 8, 5, 4, 7, 8],
                    mk[4, 1, 6, 7, 5, 2, 4, 6, 4, 5],
                    mk[2, 1, 7, 6, 8, 4, 1, 7, 2, 1],
                    mk[6, 8, 8, 2, 8, 8, 1, 1, 3, 4],
                    mk[4, 8, 4, 6, 8, 4, 8, 5, 5, 4],
                    mk[5, 2, 8, 3, 7, 5, 1, 5, 2, 6],
            ]
        )

        val endGrid = simulateOctopusGrid(octopusGrid, numGenerations = 100)

        assertEquals(
            SteppedOctopusGrid(
                mk.ndarray(
                    mk[
                            mk[0, 3, 9, 7, 6, 6, 6, 8, 6, 6],
                            mk[0, 7, 4, 9, 7, 6, 6, 9, 1, 8],
                            mk[0, 0, 5, 3, 9, 7, 6, 9, 3, 3],
                            mk[0, 0, 0, 4, 2, 9, 7, 8, 2, 2],
                            mk[0, 0, 0, 4, 2, 2, 9, 8, 9, 2],
                            mk[0, 0, 5, 3, 2, 2, 2, 8, 7, 7],
                            mk[0, 5, 3, 2, 2, 2, 2, 9, 6, 6],
                            mk[9, 3, 2, 2, 2, 2, 8, 9, 6, 6],
                            mk[7, 9, 2, 2, 2, 8, 6, 8, 6, 6],
                            mk[6, 7, 8, 9, 9, 9, 8, 7, 6, 6],
                    ]
                ), numFlashes = 1656
            ),
            endGrid
        )
    }

    @Test
    fun findFirstSynchronizedGeneration_returnsFirstGenerationInWhichAllOctopusesFlash() {
        val octopusGrid = mk.ndarray(
            mk[
                    mk[5, 4, 8, 3, 1, 4, 3, 2, 2, 3],
                    mk[2, 7, 4, 5, 8, 5, 4, 7, 1, 1],
                    mk[5, 2, 6, 4, 5, 5, 6, 1, 7, 3],
                    mk[6, 1, 4, 1, 3, 3, 6, 1, 4, 6],
                    mk[6, 3, 5, 7, 3, 8, 5, 4, 7, 8],
                    mk[4, 1, 6, 7, 5, 2, 4, 6, 4, 5],
                    mk[2, 1, 7, 6, 8, 4, 1, 7, 2, 1],
                    mk[6, 8, 8, 2, 8, 8, 1, 1, 3, 4],
                    mk[4, 8, 4, 6, 8, 4, 8, 5, 5, 4],
                    mk[5, 2, 8, 3, 7, 5, 1, 5, 2, 6],
            ]
        )

        val generation = findFirstSynchronizedGeneration(octopusGrid)

        assertEquals(195, generation)
    }
}
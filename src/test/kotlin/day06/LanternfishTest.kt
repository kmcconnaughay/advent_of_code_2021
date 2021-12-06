package day06

import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class LanternfishTest {

    @Test
    fun initializePopulationSize_countsLanternfishAtEachLifecycleStage() {
        val input = listOf("3,4,3,1,2")

        val initialPopulation = initializePopulation(input)

        assertEquals(mk.ndarray(mk[0L, 1L, 1L, 2L, 1L, 0L, 0L, 0L, 0L], dim1 = 9, dim2 = 1), initialPopulation)
    }

    @Test
    fun populationSizeAfterNSteps_modelsPopulationGrowth() {
        val initialPopulation = mk.ndarray(mk[0L, 1L, 1L, 2L, 1L, 0L, 0L, 0L, 0L], dim1 = 9, dim2 = 1)

        val gen1Size = populationSizeAfterNSteps(initialPopulation, 1)
        val gen2Size = populationSizeAfterNSteps(initialPopulation, 2)
        val gen3Size = populationSizeAfterNSteps(initialPopulation, 3)
        val gen4Size = populationSizeAfterNSteps(initialPopulation, 4)
        val gen5Size = populationSizeAfterNSteps(initialPopulation, 5)
        val gen6Size = populationSizeAfterNSteps(initialPopulation, 6)
        val gen7Size = populationSizeAfterNSteps(initialPopulation, 7)
        val gen8Size = populationSizeAfterNSteps(initialPopulation, 8)
        val gen9Size = populationSizeAfterNSteps(initialPopulation, 9)
        val gen10Size = populationSizeAfterNSteps(initialPopulation, 10)
        val gen11Size = populationSizeAfterNSteps(initialPopulation, 11)
        val gen12Size = populationSizeAfterNSteps(initialPopulation, 12)
        val gen13Size = populationSizeAfterNSteps(initialPopulation, 13)
        val gen14Size = populationSizeAfterNSteps(initialPopulation, 14)
        val gen15Size = populationSizeAfterNSteps(initialPopulation, 15)
        val gen16Size = populationSizeAfterNSteps(initialPopulation, 16)
        val gen17Size = populationSizeAfterNSteps(initialPopulation, 17)
        val gen18Size = populationSizeAfterNSteps(initialPopulation, 18)

        assertEquals(5, gen1Size)
        assertEquals(6, gen2Size)
        assertEquals(7, gen3Size)
        assertEquals(9, gen4Size)
        assertEquals(10, gen5Size)
        assertEquals(10, gen6Size)
        assertEquals(10, gen7Size)
        assertEquals(10, gen8Size)
        assertEquals(11, gen9Size)
        assertEquals(12, gen10Size)
        assertEquals(15, gen11Size)
        assertEquals(17, gen12Size)
        assertEquals(19, gen13Size)
        assertEquals(20, gen14Size)
        assertEquals(20, gen15Size)
        assertEquals(21, gen16Size)
        assertEquals(22, gen17Size)
        assertEquals(26, gen18Size)
    }
}
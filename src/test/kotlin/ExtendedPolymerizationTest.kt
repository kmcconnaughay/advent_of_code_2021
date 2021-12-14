import day14.computeElementHistogram
import day14.countNGrams
import day14.followPolymerizationInstructions
import day14.insertPairs
import day14.parsePolymerizationInstructions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ExtendedPolymerizationTest {

    @Test
    fun insertPairs_expandsPolymerizationTemplate() {
        val polymerizationInstructions = parsePolymerizationInstructions(
            listOf(
                "NNCB",
                "CH -> B",
                "HH -> N",
                "CB -> H",
                "NH -> C",
                "HB -> C",
                "HC -> B",
                "HN -> C",
                "NN -> C",
                "BH -> H",
                "NC -> B",
                "NB -> B",
                "BN -> B",
                "BB -> N",
                "BC -> B",
                "CC -> N",
                "CN -> C",
            )
        )

        val expansion1 =
            insertPairs(polymerizationInstructions.polymerPairs, polymerizationInstructions.pairInsertionRules)
        val expansion2 =
            insertPairs(expansion1, polymerizationInstructions.pairInsertionRules)
        val expansion3 =
            insertPairs(expansion2, polymerizationInstructions.pairInsertionRules)
        val expansion4 =
            insertPairs(expansion3, polymerizationInstructions.pairInsertionRules)

        assertEquals("NCNBCHB".countNGrams(n = 2), expansion1)
        assertEquals("NBCCNBBBCBHCB".countNGrams(n = 2), expansion2)
        assertEquals("NBBBCNCCNBBNBNBBCHBHHBCHB".countNGrams(n = 2), expansion3)
        assertEquals("NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB".countNGrams(n = 2), expansion4)
    }

    @Test
    fun computeElementHistogram_returnsTheNumberOfOccurrencesOfEachElement() {
        val polymerizationInstructions = parsePolymerizationInstructions(
            listOf(
                "NNCB",
                "CH -> B",
                "HH -> N",
                "CB -> H",
                "NH -> C",
                "HB -> C",
                "HC -> B",
                "HN -> C",
                "NN -> C",
                "BH -> H",
                "NC -> B",
                "NB -> B",
                "BN -> B",
                "BB -> N",
                "BC -> B",
                "CC -> N",
                "CN -> C",
            )
        )
        val polymerPairs = insertPairs(
            insertPairs(
                polymerizationInstructions.polymerPairs,
                polymerizationInstructions.pairInsertionRules
            ), polymerizationInstructions.pairInsertionRules
        )

        val histogram = computeElementHistogram(polymerizationInstructions.polymerTemplate, polymerPairs)

        assertEquals("NBCCNBBBCBHCB".histogram(), histogram)
    }

    @Test
    fun followPolymerizationInstructions_returnsDifferenceBetweenMostAndLeastCommonElements() {
        val polymerizationInstructions = parsePolymerizationInstructions(
            listOf(
                "NNCB",
                "CH -> B",
                "HH -> N",
                "CB -> H",
                "NH -> C",
                "HB -> C",
                "HC -> B",
                "HN -> C",
                "NN -> C",
                "BH -> H",
                "NC -> B",
                "NB -> B",
                "BN -> B",
                "BB -> N",
                "BC -> B",
                "CC -> N",
                "CN -> C",
            )
        )

        val result = followPolymerizationInstructions(polymerizationInstructions, numSteps = 10)

        assertEquals(1588, result)
    }

    private fun String.histogram(): Map<Char, Long> =
        this.groupingBy { it }.eachCount().mapValues { it.value.toLong() }
}
package day14

data class PolymerizationInstructions(
    val polymerTemplate: String,
    val polymerPairs: Map<String, Long>,
    val pairInsertionRules: Map<String, List<String>>
)

fun followPolymerizationInstructions(polymerizationInstructions: PolymerizationInstructions, numSteps: Int): Long {
    var polymerPairs = polymerizationInstructions.polymerPairs
    for (i in 1..numSteps) {
        polymerPairs = insertPairs(polymerPairs, polymerizationInstructions.pairInsertionRules)
    }
    val elementHistogram = computeElementHistogram(polymerizationInstructions.polymerTemplate, polymerPairs)
    return elementHistogram.values.maxOrNull()!! - elementHistogram.values.minOrNull()!!
}

fun computeElementHistogram(polymerTemplate: String, polymerPairs: Map<String, Long>): Map<Char, Long> {
    val result = mutableMapOf<Char, Long>()

    for ((pair, pairCount) in polymerPairs) {
        result.compute(pair[0]) { _, elementCount -> (elementCount ?: 0L) + pairCount }
        result.compute(pair[1]) { _, elementCount -> (elementCount ?: 0L) + pairCount }
    }

    result.replaceAll { element, count ->
        if (element == polymerTemplate.first() || element == polymerTemplate.last()) {
            (count + 1) / 2
        } else {
            count / 2
        }
    }

    return result
}

fun insertPairs(polymerPairs: Map<String, Long>, pairInsertionRules: Map<String, List<String>>): Map<String, Long> {
    val newPolymerPairs = mutableMapOf<String, Long>()

    for ((pair, count) in polymerPairs) {
        val insertions = pairInsertionRules.getOrDefault(pair, listOf())
        for (insertion in insertions) {
            newPolymerPairs.compute(insertion) { _, newCount -> (newCount ?: 0) + count }
        }
    }

    return newPolymerPairs
}

fun parsePolymerizationInstructions(input: List<String>): PolymerizationInstructions {
    val polymerTemplate = input[0]
    val polymerPairs = polymerTemplate.countNGrams(n = 2)
    val pairInsertionRules = parsePairInsertionRules(input.subList(1, input.size))
    return PolymerizationInstructions(polymerTemplate, polymerPairs, pairInsertionRules)
}

fun parsePairInsertionRules(pairInsertionRules: List<String>): Map<String, List<String>> =
    pairInsertionRules.associate { pairInsertionRule ->
        val (pair, insert) = pairInsertionRule.split(" -> ")
        pair to listOf(pair[0] + insert, insert + pair[1])
    }

fun String.countNGrams(n: Int): Map<String, Long> =
    this.windowed(size = n).groupingBy { it }.eachCount().mapValues { (_, count) -> count.toLong() }

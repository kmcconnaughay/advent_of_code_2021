package day03

data class PowerConsumption(val gammaRate: Int, val epsilonRate: Int, val powerConsumption: Int)

data class LifeSupportRating(val oxygenGeneratorRating: Int, val co2ScrubberRating: Int, val lifeSupportRating: Int)

fun computeLifeSupportRating(diagnosticReport: List<String>): LifeSupportRating {
    val oxygenGeneratorRating = computeOxygenGeneratorRating(diagnosticReport)
    val co2ScrubberRating = computeCo2ScrubberRating(diagnosticReport)
    return LifeSupportRating(
        oxygenGeneratorRating = oxygenGeneratorRating,
        co2ScrubberRating = co2ScrubberRating,
        lifeSupportRating = oxygenGeneratorRating * co2ScrubberRating
    )
}

private fun computeOxygenGeneratorRating(diagnosticReport: List<String>): Int {
    var matches = diagnosticReport
    for (i in matches.indices) {
        val numOnes = matches.map { reading -> reading[i] }.count { bit -> bit == '1' }
        val numZeroes = matches.size - numOnes
        matches = if (numOnes >= numZeroes) {
            matches.filter { reading -> reading[i] == '1' }
        } else {
            matches.filter { reading -> reading[i] == '0' }
        }

        if (matches.size == 1) {
            return Integer.parseInt(matches[0], 2)
        }
    }

    return 0
}

private fun computeCo2ScrubberRating(diagnosticReport: List<String>): Int {
    var matches = diagnosticReport
    for (i in matches.indices) {
        val numOnes = matches.map { reading -> reading[i] }.count { bit -> bit == '1' }
        val numZeroes = matches.size - numOnes
        matches = if (numOnes >= numZeroes) {
            matches.filter { reading -> reading[i] == '0' }
        } else {
            matches.filter { reading -> reading[i] == '1' }
        }

        if (matches.size == 1) {
            return Integer.parseInt(matches[0], 2)
        }
    }

    return 0
}

fun computePowerConsumption(diagnosticReport: List<String>): PowerConsumption {
    val bitCounts = computeBitCounts(diagnosticReport)
    val gammaRate = computeGammaRate(bitCounts)
    val epsilonRate = computeEpsilonRate(bitCounts)
    return PowerConsumption(
        gammaRate = gammaRate,
        epsilonRate = epsilonRate,
        powerConsumption = gammaRate * epsilonRate
    )
}

private fun computeEpsilonRate(bitCounts: List<BitCount>): Int {
    return bitCounts.map { bitCount -> if (bitCount.numZeroes > bitCount.numOnes) 1 else 0 }.toBinaryNumber()
}

private fun computeGammaRate(bitCounts: List<BitCount>): Int {
    return bitCounts.map { bitCount -> if (bitCount.numOnes > bitCount.numZeroes) 1 else 0 }.toBinaryNumber()
}

private fun List<Int>.toBinaryNumber(): Int {
    var number = 0
    for ((index, bit) in this.withIndex()) {
        number = number or (bit shl (this.size - index - 1))
    }
    return number
}

private data class BitCount(var numZeroes: Int = 0, var numOnes: Int = 0)

/**
 * Returns the number of ones and zeros at each index in the given list of strings of uniform-length binary numbers. The
 * result is read left to right, meaning that the first element in the list corresponds to the largest bit, the second
 * element corresponds to the second-largest bit, and so on.
 */
private fun computeBitCounts(diagnosticReport: List<String>): List<BitCount> {
    if (diagnosticReport.isEmpty()) {
        return listOf()
    }

    val numBits = diagnosticReport[0].length

    val counts = MutableList(size = numBits) { BitCount() }
    for (diagnostic in diagnosticReport) {
        for (i in 0 until numBits) {
            if (diagnostic[i] == '0') {
                counts[i].numZeroes += 1
            } else {
                counts[i].numOnes += 1
            }
        }
    }

    return counts
}
package day08

enum class SignalLine {
    A,
    B,
    C,
    D,
    E,
    F,
    G;

    companion object {
        fun fromLetter(letter: Char): SignalLine {
            return enumValueOf(letter.uppercase())
        }
    }
}

data class SignalPattern(val activeSignalLines: Set<SignalLine>) {

    fun subtract(other: SignalPattern): Set<SignalLine> {
        return activeSignalLines.subtract(other.activeSignalLines)
    }

    val size: Int
        get() {
            return activeSignalLines.size
        }

    companion object {
        fun parse(input: String): SignalPattern {
            return SignalPattern(input.map(SignalLine::fromLetter).toSet())
        }
    }
}

data class Display(val signalPatterns: List<SignalPattern>, val outputValue: List<SignalPattern>) {

    companion object {
        fun parse(input: String): Display {
            val (unparsedSignalPatterns, unparsedOutputValue) = input.split("|")
            return Display(
                signalPatterns = unparsedSignalPatterns.trim().split(" ").map(SignalPattern::parse),
                outputValue = unparsedOutputValue.trim().split(" ").map(SignalPattern::parse)
            )
        }
    }
}

fun countOnesFoursSevensAndEightsInOutputValues(displays: List<String>): Int {
    return displays.map(Display::parse).flatMap(Display::outputValue)
        .count { it.activeSignalLines.size in setOf(2, 3, 4, 7) }
}

fun sumOutputValues(displays: List<String>): Int {
    return displays.sumOf(::decodeOutputValue)
}

fun decodeOutputValue(unparsedDisplay: String): Int {
    val display = Display.parse(unparsedDisplay)
    val unknownSignalPatterns: MutableSet<SignalPattern> = display.signalPatterns.toMutableSet()

    val one = unknownSignalPatterns.popOnly { it.size == 2 }
    val four = unknownSignalPatterns.popOnly { it.activeSignalLines.size == 4 }
    val seven = unknownSignalPatterns.popOnly { it.activeSignalLines.size == 3 }
    val eight = unknownSignalPatterns.popOnly { it.activeSignalLines.size == 7 }
    val three = unknownSignalPatterns.popOnly { it.size == 5 && one.subtract(it).isEmpty() }
    val five = unknownSignalPatterns.popOnly { it.size == 5 && four.subtract(it).size == 1 }
    val two = unknownSignalPatterns.popOnly { it.size == 5 }
    val six = unknownSignalPatterns.popOnly { one.subtract(it).size == 1 }
    val zero = unknownSignalPatterns.popOnly { four.subtract(it).size == 1 }
    val nine = unknownSignalPatterns.first()

    val digits = display.outputValue.map { digit ->
        when (digit) {
            zero -> 0
            one -> 1
            two -> 2
            three -> 3
            four -> 4
            five -> 5
            six -> 6
            seven -> 7
            eight -> 8
            nine -> 9
            else -> {
                throw IllegalStateException()
            }
        }
    }

    var number = 0
    var multiplier = 1
    for (digit in digits.reversed()) {
        number += digit * multiplier
        multiplier *= 10
    }
    return number
}

private fun <T> Set<T>.only(predicate: (T) -> Boolean): T {
    val matches = this.filter { predicate.invoke(it) }

    if (matches.size == 1) {
        return matches[0]
    } else {
        throw IllegalArgumentException("The given predicate matched ${matches.size} elements")
    }
}

private fun <T> MutableSet<T>.popOnly(predicate: (T) -> Boolean): T {
    val match = this.only(predicate)
    this.remove(match)
    return match
}
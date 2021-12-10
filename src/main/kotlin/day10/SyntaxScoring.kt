package day10

sealed class SyntaxCheckResult(line: String)

data class Valid(val line: String) : SyntaxCheckResult(line)

data class Corrupt(val line: String, val indexOfFirstIllegalCharacter: Int, val expected: Char, val actual: Char) :
    SyntaxCheckResult(line)

data class Incomplete(val line: String, val autoCompletion: String) : SyntaxCheckResult(line)

private val braces = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>',
)

private val syntaxErrorScores = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137,
)

private val autoCompleteScores = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4
)

fun computeAutoCompleteScore(lines: List<String>): Long {
    val individualScores =
        lines.map { checkSyntax(it) }.filterIsInstance<Incomplete>().map { computeAutoCompleteScore(it) }.sorted()
    return individualScores[individualScores.size / 2]
}

private fun computeAutoCompleteScore(incomplete: Incomplete): Long {
    var score = 0L
    for (brace in incomplete.autoCompletion) {
        score *= 5
        score += autoCompleteScores[brace]!!
    }
    return score
}

fun computeSyntaxErrorScore(lines: List<String>): Long {
    var score = 0L
    for (line in lines) {
        val syntaxCheckResult = checkSyntax(line)
        if (syntaxCheckResult is Corrupt) {
            score += syntaxErrorScores[syntaxCheckResult.actual]!!
        }
    }
    return score
}

fun checkSyntax(line: String): SyntaxCheckResult {
    val stack = ArrayDeque<Char>()
    for ((index, character) in line.withIndex()) {
        if (braces.containsKey(character)) {
            stack.addFirst(character)
            continue
        }

        val openingBrace = stack.removeFirst()
        val closingBrace = braces[openingBrace]
        if (character != closingBrace) {
            return Corrupt(
                line = line,
                indexOfFirstIllegalCharacter = index,
                expected = closingBrace!!,
                actual = character
            )
        }
    }

    return if (stack.isEmpty()) {
        Valid(line = line)
    } else {
        Incomplete(
            line = line,
            stack.map { braces[it]!! }.joinToString(separator = "") { it.toString() })
    }
}
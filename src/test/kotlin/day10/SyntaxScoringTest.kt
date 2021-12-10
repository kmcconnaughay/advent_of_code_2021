package day10

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals
import kotlin.test.assertIs


internal class SyntaxScoringTest {

    @DisplayName("checkSyntax_validString_returnsValid")
    @ParameterizedTest
    @ValueSource(
        strings = ["", "()", "[]", "([])", "{()()()}", "<([{}])>", "[<>({}){}[([])<>]]", "(((((((((())))))))))"]
    )
    fun checkSyntax_validString_returnsValid(line: String) {
        assertIs<Valid>(checkSyntax(line))
    }

    @DisplayName("checkSyntax_incompleteString_returnsIncompleteWithAutoCompletion")
    @ParameterizedTest
    @CsvSource(
        value = [
            // LINE,                       COMPLETION
            "  '[({(<(())[]>[[{[]{<()<>>', '}}]])})]'",
            "  '[(()[<>])]({[<{<<[]>>(',   ')}>]})'",
            "  '(((({<>}<{<{<>}{[]{[]{}',  '}}>}>))))'",
            "  '{<[[]]>}<{[{[{[]{()[[[]',  ']]}}]}]}>'",
            "  '<{([{{}}[<[[[<>{}]]]>[]]', '])}>'",
        ]
    )
    fun checkSyntax_incompleteStrings_returnsAutoCompletion(line: String, completion: String) {
        assertEquals(Incomplete(line = line, autoCompletion = completion), checkSyntax(line))
    }

    @DisplayName("checkSyntax_corruptedString_returnsCorrupt")
    @ParameterizedTest
    @CsvSource(
        value = [
            // LINE,                       INDEX_OF_FIRST_ILLEGAL_CHARACTER, EXPECTED, ACTUAL
            "  '(]',                       1,                                ')',      ']'",
            "  '{()()()>',                 7,                                '}',      '>'",
            "  '(((()))}',                 7,                                ')',      '}'",
            "  '<([]){()}[{}])',           13,                               '>',      ')'",
            "  '{([(<{}[<>[]}>{[]{[(<()>', 12,                               ']',      '}'",
            "  '[[<[([]))<([[{}[[()]]]',   8,                                ']',      ')'",
            "  '[{[{({}]{}}([{[{{{}}([]',  7,                                ')',      ']'",
            "  '[<(<(<(<{}))><([]([]()',   10,                               '>',      ')'",
            "  '<{([([[(<>()){}]>(<<{{',   16,                               ']',      '>'",
        ]
    )
    fun checkSyntax_corruptedString_returnsCorrupt(
        line: String,
        indexOfFirstIllegalCharacter: Int,
        expected: Char,
        actual: Char
    ) {
        assertEquals(
            Corrupt(
                line = line,
                indexOfFirstIllegalCharacter = indexOfFirstIllegalCharacter,
                expected = expected,
                actual = actual
            ),
            checkSyntax(line)
        )
    }

    @Test
    fun computeSyntaxErrorScore_computesScoreFromFirstIllegalCharacterInCorruptLines() {
        val lines = listOf(
            "[({(<(())[]>[[{[]{<()<>>",
            "[(()[<>])]({[<{<<[]>>(",
            "{([(<{}[<>[]}>{[]{[(<()>",
            "(((({<>}<{<{<>}{[]{[]{}",
            "[[<[([]))<([[{}[[()]]]",
            "[{[{({}]{}}([{[{{{}}([]",
            "{<[[]]>}<{[{[{[]{()[[[]",
            "[<(<(<(<{}))><([]([]()",
            "<{([([[(<>()){}]>(<<{{",
            "<{([{{}}[<[[[<>{}]]]>[]]",
        )

        val score = computeSyntaxErrorScore(lines)

        assertEquals(score, 26397)
    }

    @Test
    fun computeAutocompleteScore_returnsScores() {
        val lines = listOf(
            "[({(<(())[]>[[{[]{<()<>>",
            "[(()[<>])]({[<{<<[]>>(",
            "{([(<{}[<>[]}>{[]{[(<()>",
            "(((({<>}<{<{<>}{[]{[]{}",
            "[[<[([]))<([[{}[[()]]]",
            "[{[{({}]{}}([{[{{{}}([]",
            "{<[[]]>}<{[{[{[]{()[[[]",
            "[<(<(<(<{}))><([]([]()",
            "<{([([[(<>()){}]>(<<{{",
            "<{([{{}}[<[[[<>{}]]]>[]]",
        )

        val score = computeAutoCompleteScore(lines)

        assertEquals(288957, score)
    }
}
package day20

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class TrenchMapTest {

    private val exampleInput = listOf(
        "..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#",
        "#..#.",
        "#....",
        "##..#",
        "..#..",
        "..###",
    )

    @Test
    fun window() {
        val trenchMap = parseTrenchMap(exampleInput)

        val window = trenchMap.image.window(row = 2, column = 2)

        assertEquals(
            listOf(
                Pixel.DARK,
                Pixel.DARK,
                Pixel.DARK,
                Pixel.LIGHT,
                Pixel.DARK,
                Pixel.DARK,
                Pixel.DARK,
                Pixel.LIGHT,
                Pixel.DARK,
            ), window
        )
    }

    @Test
    fun getImageEnhancementAlgorithmIndex() {
        assertEquals(
            0,
            listOf(
                Pixel.DARK,
                Pixel.DARK,
                Pixel.DARK,
                Pixel.DARK,
                Pixel.DARK,
                Pixel.DARK,
                Pixel.DARK,
                Pixel.DARK,
                Pixel.DARK,
            ).getImageEnhancementAlgorithmIndex()
        )
        assertEquals(
            34,
            listOf(
                Pixel.DARK,
                Pixel.DARK,
                Pixel.DARK,
                Pixel.LIGHT,
                Pixel.DARK,
                Pixel.DARK,
                Pixel.DARK,
                Pixel.LIGHT,
                Pixel.DARK,
            ).getImageEnhancementAlgorithmIndex()
        )
        assertEquals(
            511,
            listOf(
                Pixel.LIGHT,
                Pixel.LIGHT,
                Pixel.LIGHT,
                Pixel.LIGHT,
                Pixel.LIGHT,
                Pixel.LIGHT,
                Pixel.LIGHT,
                Pixel.LIGHT,
                Pixel.LIGHT,
            ).getImageEnhancementAlgorithmIndex()
        )
    }

    @Test
    fun enhance() {
        val trenchMap = parseTrenchMap(exampleInput)
        val enhanced =
            "" +
                    ".......#.\n" +
                    ".#..#.#..\n" +
                    "#.#...###\n" +
                    "#...##.#.\n" +
                    "#.....#.#\n" +
                    ".#.#####.\n" +
                    "..#.#####\n" +
                    "...##.##.\n" +
                    "....###..\n"

        assertEquals(enhanced, trenchMap.enhance(numRounds = 2).toString())
    }
}

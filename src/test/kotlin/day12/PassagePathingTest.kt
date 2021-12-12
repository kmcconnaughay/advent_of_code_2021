package day12

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PassagePathingTest {

    @Test
    fun countAllPaths_canOnlyVisitSmallCavesOnce_returnsNumberOfUniquePathsThroughCaveSystem() {
        val caveSystem = parseCaveSystem(
            listOf(
                "fs-end",
                "he-DX",
                "fs-he",
                "start-DX",
                "pj-DX",
                "end-zg",
                "zg-sl",
                "zg-pj",
                "pj-he",
                "RW-he",
                "fs-DX",
                "pj-RW",
                "zg-RW",
                "start-pj",
                "he-WI",
                "zg-he",
                "pj-fs",
                "start-RW",
            )
        )

        val numPaths = countAllPaths(caveSystem, allowDoubleEntryToOneSmallCave = false)

        assertEquals(226L, numPaths)
    }

    @Test
    fun countAllPaths_canVisitASingleSmallCaveTwice_returnsNumberOfUniquePathsThroughCaveSystem() {
        val caveSystem = parseCaveSystem(
            listOf(
                "start-A",
                "start-b",
                "A-c",
                "A-b",
                "b-d",
                "A-end",
                "b-end",
            )
        )
//        val caveSystem = parseCaveSystem(
//            listOf(
//                "fs-end",
//                "he-DX",
//                "fs-he",
//                "start-DX",
//                "pj-DX",
//                "end-zg",
//                "zg-sl",
//                "zg-pj",
//                "pj-he",
//                "RW-he",
//                "fs-DX",
//                "pj-RW",
//                "zg-RW",
//                "start-pj",
//                "he-WI",
//                "zg-he",
//                "pj-fs",
//                "start-RW",
//            )
//        )

        val numPaths = countAllPaths(caveSystem, allowDoubleEntryToOneSmallCave = true)

        assertEquals(36L, numPaths)
    }
}
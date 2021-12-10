package day06

import org.jetbrains.kotlinx.multik.api.linalg.dot
import org.jetbrains.kotlinx.multik.api.linalg.eig
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.complex.ComplexDouble
import org.jetbrains.kotlinx.multik.ndarray.data.D1
import org.jetbrains.kotlinx.multik.ndarray.data.D2
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.NDArray
import org.jetbrains.kotlinx.multik.ndarray.data.set
import org.jetbrains.kotlinx.multik.ndarray.operations.sum

fun populationSizeAfterNSteps(initialPopulation: D2Array<Long>, steps: Int): Long {
    var population = initialPopulation
    for (i in 1..steps) {
        // If this needed to be faster, I could compute the eigendecomposition of the reproduction matrix to reduce
        // the number of multiplication operations to a linear multiple of the steps parameter. I decided against doing
        // it because:
        //
        // 1. I'm lazy
        // 2. This seems fast enough
        // 3. The eigenvalues and eigenvectors are not integer values; adventofcode requires an integer answer, so I
        //    don't want to deal with rounding errors introduced by exponentiating the eigenvalues 256 times, nor do I
        //    want to fight Kotlin's type system to coerce everything into the proper numerical types and matrix shapes.
        population = reproductionMatrix dot population

    }
    return population.sum()
}

fun initializePopulation(input: List<String>): D2Array<Long> {
    val lanternfish = input[0].split(",").map { it.toInt() }
    val populationCounts = lanternfish.groupBy { it }
        .mapValues { (_, lanternfishByLifecycleStage) -> lanternfishByLifecycleStage.size }
    val populationVector = mk.zeros<Long>(dim1 = 9, dim2 = 1)
    for ((timeToReproduction, count) in populationCounts) {
        populationVector[timeToReproduction, 0] = count.toLong()
    }
    return populationVector
}

private val reproductionMatrix: D2Array<Long> =
    mk.ndarray(
        mk[
                mk[0, 1, 0, 0, 0, 0, 0, 0, 0],
                mk[0, 0, 1, 0, 0, 0, 0, 0, 0],
                mk[0, 0, 0, 1, 0, 0, 0, 0, 0],
                mk[0, 0, 0, 0, 1, 0, 0, 0, 0],
                mk[0, 0, 0, 0, 0, 1, 0, 0, 0],
                mk[0, 0, 0, 0, 0, 0, 1, 0, 0],
                mk[1, 0, 0, 0, 0, 0, 0, 1, 0],
                mk[0, 0, 0, 0, 0, 0, 0, 0, 1],
                mk[1, 0, 0, 0, 0, 0, 0, 0, 0],
        ]
    )

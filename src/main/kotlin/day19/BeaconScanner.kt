package day19

import org.jetbrains.kotlinx.multik.api.abs
import org.jetbrains.kotlinx.multik.api.linalg.dot
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.default.linalg.DefaultLinAlg.pow
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.operations.minus
import org.jetbrains.kotlinx.multik.ndarray.operations.plus
import org.jetbrains.kotlinx.multik.ndarray.operations.sum
import org.typemeta.funcj.data.Chr
import org.typemeta.funcj.parser.Input
import org.typemeta.funcj.parser.Parser
import org.typemeta.funcj.parser.Text.chr
import org.typemeta.funcj.parser.Text.intr

fun countBeacons(scannerReadings: Set<ScannerReading>): Int =
    scannerReadings.flatMap { scannerReading -> scannerReading.beacons }.toSet().size

fun largestManhattanDistance(scannerReadings: Set<ScannerReading>): Int =
    scannerReadings.cartesianProduct(scannerReadings)
        .maxOf { (a, b) -> manhattanDistance(a.scannerLocation, b.scannerLocation) }

fun normalizeScannerReadings(scannerReadings: List<ScannerReading>): Set<ScannerReading> {
    val anchors: MutableSet<ScannerReading> = mutableSetOf(scannerReadings.first())
    val denormalizedReadings: MutableSet<ScannerReading> =
        scannerReadings.subList(1, scannerReadings.size).toMutableSet()

    while (denormalizedReadings.isNotEmpty()) {
        val normalized = denormalizedReadings.cartesianProduct(anchors)
            .map { (denormalized, anchor) -> Pair(denormalized.normalizeBy(anchor), denormalized) }
            .filterNot { it.first == null }
        normalized.forEach { anchors.add(it.first!!); denormalizedReadings.remove(it.second) }
    }

    return anchors
}

data class Transformation(val rotation: D2Array<Int>, val translation: D2Array<Int>)

data class ScannerReading(val id: Int, val scannerLocation: D2Array<Int>, val beacons: Set<D2Array<Int>>) {
    private val pairwiseDistances: Map<Int, Set<D2Array<Int>>> = computePairwiseDistances()

    fun normalizeBy(anchor: ScannerReading): ScannerReading? {
        val equalDistances = pairwiseDistances.keys.intersect(anchor.pairwiseDistances.keys)
        if (equalDistances.size < 12) {
            return null
        }

        val denormalizedBeacons = pairwiseDistances.filterKeys { distance -> equalDistances.contains(distance) }
            .flatMap { (_, beacons) -> beacons }.toSet()
        val anchoredBeacons = anchor.pairwiseDistances.filterKeys { distance -> equalDistances.contains(distance) }
            .flatMap { (_, beacons) -> beacons }.toSet()

        val transformationHistogram = mutableMapOf<Transformation, Int>()
        for ((denormalizedBeacon, anchorBeacon) in denormalizedBeacons.cartesianProduct(anchoredBeacons)) {
            for (rotation in ROTATIONS) {
                val rotated = rotation dot denormalizedBeacon
                val translation = anchorBeacon - rotated
                val transformation = Transformation(rotation = rotation, translation = translation)
                transformationHistogram[transformation] = (transformationHistogram[transformation] ?: 0) + 1
            }
        }

        val (bestTransformation, count) = transformationHistogram.maxByOrNull { (_, count) -> count }!!
        if (count < 12) {
            return null
        }

        val transformedBeacons =
            beacons.map { (bestTransformation.rotation dot it) + bestTransformation.translation }.toSet()
        return ScannerReading(id = id, scannerLocation = bestTransformation.translation, beacons = transformedBeacons)
    }

    private fun computePairwiseDistances(): Map<Int, Set<D2Array<Int>>> {
        val distances = mutableMapOf<Int, MutableSet<D2Array<Int>>>()
        for (pair in beacons.cartesianProduct(beacons).filterNot { (first, second) -> first == second }) {
            val distance = manhattanDistance(pair.first, pair.second)
            distances.putIfAbsent(distance, mutableSetOf())
            distances[distance]?.add(pair.first)
            distances[distance]?.add(pair.second)
        }
        return distances
    }
}

private fun <M, N> Set<M>.cartesianProduct(other: Set<N>): Set<Pair<M, N>> =
    this.flatMap { a -> other.map { b -> Pair(a, b) } }.toSet()

private fun manhattanDistance(a: D2Array<Int>, b: D2Array<Int>): Int {
    require(a.shape.contains(1))
    return abs(a - b).sum()
}

private val ROTATE_X_90: D2Array<Int> =
    mk.ndarray(mk[mk[1, 0, 0], mk[0, 0, -1], mk[0, 1, 0]])

private val ROTATE_Y_90: D2Array<Int> =
    mk.ndarray(mk[mk[0, 0, 1], mk[0, 1, 0], mk[-1, 0, 0]])

private val ROTATE_Z_90: D2Array<Int> =
    mk.ndarray(mk[mk[0, -1, 0], mk[1, 0, 0], mk[0, 0, 1]])

val ROTATIONS: Set<D2Array<Int>> =
    (0..3).flatMap { numXRotations ->
        (0..3).flatMap { numYRotations ->
            (0..3).map { numZRotations ->
                pow(ROTATE_X_90, numXRotations) dot pow(ROTATE_Y_90, numYRotations) dot pow(ROTATE_Z_90, numZRotations)
            }
        }
    }.toSet()

private val scannerMatcher = Regex(pattern = "--- scanner \\d+ ---")

private val point3DParser: Parser<Chr, D2Array<Int>> =
    intr.andL(chr(',')).and(intr).andL(chr(',')).and(intr).map { x, y, z -> mk.ndarray(mk[mk[x], mk[y], mk[z]]) }

fun parseScannerReadings(input: List<String>): List<ScannerReading> =
    input.split { scannerMatcher.matches(it) }
        .mapIndexed { index, beacons ->
            ScannerReading(
                id = index,
                scannerLocation = mk.zeros(dim1 = 3, dim2 = 1),
                beacons = beacons.map { beacon -> point3DParser.parse(Input.of(beacon)).orThrow }.toSet()
            )
        }

private fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> {
    val outer = mutableListOf<List<T>>()
    var inner = mutableListOf<T>()
    for (element in this) {
        if (predicate.invoke(element)) {
            if (inner.isNotEmpty()) {
                outer.add(inner)
                inner = mutableListOf()
            }
        } else {
            inner.add(element)
        }
    }

    if (inner.isNotEmpty()) {
        outer.add(inner)
    }
    return outer
}



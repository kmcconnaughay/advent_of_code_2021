package day22

import org.typemeta.funcj.data.Chr
import org.typemeta.funcj.parser.Input
import org.typemeta.funcj.parser.Parser
import org.typemeta.funcj.parser.Text.alpha
import org.typemeta.funcj.parser.Text.chr
import org.typemeta.funcj.parser.Text.lng
import org.typemeta.funcj.parser.Text.string
import java.lang.Long.max
import java.lang.Long.min

enum class Power {
    ON, OFF;

    companion object {
        val parser: Parser<Chr, Power> =
            alpha.many1().map { enumValueOf<Power>(it.joinToString(separator = "").uppercase()) }
    }
}

fun LongRange.intersects(other: LongRange): Boolean = first <= other.last && last >= other.first

fun LongRange.envelops(other: LongRange): Boolean = first <= other.first && last >= other.last

fun LongRange.intersection(other: LongRange): LongRange = max(first, other.first)..min(last, other.last)

fun LongRange.length(): Long = if (isEmpty()) {
    0
} else {
    last - first + 1
}

data class Cuboid(val xRange: LongRange, val yRange: LongRange, val zRange: LongRange) {

    fun volume(): Long = xRange.length() * yRange.length() * zRange.length()

    fun intersect(other: Cuboid): Cuboid? {
        return if (intersects(other)) {
            Cuboid(
                xRange = xRange.intersection(other.xRange),
                yRange = yRange.intersection(other.yRange),
                zRange = zRange.intersection(other.zRange)
            )
        } else {
            null
        }
    }

    fun intersects(other: Cuboid): Boolean =
        xRange.intersects(other.xRange) && yRange.intersects(other.yRange) && zRange.intersects(other.zRange)

    fun envelops(other: Cuboid): Boolean =
        xRange.envelops(other.xRange) && yRange.envelops(other.yRange) && zRange.envelops(other.zRange)

    fun difference(other: Cuboid): Set<Cuboid> {
        if (other.envelops(this)) {
            return hashSetOf()
        }

        val overlap = intersect(other) ?: return hashSetOf(this)

        val subXRanges =
            hashSetOf(xRange.first until overlap.xRange.first, overlap.xRange, (overlap.xRange.last + 1)..xRange.last)
        val subYRanges =
            hashSetOf(yRange.first until overlap.yRange.first, overlap.yRange, (overlap.yRange.last + 1)..yRange.last)
        val subCuboids: MutableSet<Cuboid> = subXRanges.flatMap { xRange ->
            subYRanges.map { yRange -> Cuboid(xRange = xRange, yRange = yRange, zRange = overlap.zRange) }
        }.toHashSet()
        subCuboids.add(Cuboid(xRange = xRange, yRange = yRange, zRange = zRange.first until overlap.zRange.first))
        subCuboids.add(Cuboid(xRange = xRange, yRange = yRange, zRange = (overlap.zRange.last + 1)..zRange.last))
        return subCuboids.filterNot { it.volume() == 0L || other.envelops(it) }.toSet()
    }

    companion object {
        private val rangeParser: Parser<Chr, LongRange> =
            lng.andL(string("..")).and(lng).map { lowerBound, upperBound -> lowerBound..upperBound }

        val parser: Parser<Chr, Cuboid> =
            string("x=").andR(rangeParser).andL(string(",y=")).and(rangeParser).andL(string(",z=")).and(rangeParser)
                .map { xRange, yRange, zRange -> Cuboid(xRange = xRange, yRange = yRange, zRange = zRange) }
    }
}

data class RebootStep(val power: Power, val cuboid: Cuboid) {

    companion object {
        val parser: Parser<Chr, RebootStep> = Power.parser.andL(chr(' ')).and(Cuboid.parser)
            .map { power, cuboid -> RebootStep(power = power, cuboid = cuboid) }
    }
}

fun parseRebootSequence(input: List<String>): List<RebootStep> = input.map {
    RebootStep.parser.parse(Input.of(it)).orThrow
}

fun rebootReactor(rebootSequence: List<RebootStep>): Long {
    var poweredOn: Set<Cuboid> = hashSetOf()
    for (rebootStep in rebootSequence) {
        poweredOn = when (rebootStep.power) {
            Power.ON -> setOf(rebootStep.cuboid) + poweredOn.flatMap { it.difference(rebootStep.cuboid) }.toSet()
            Power.OFF -> poweredOn.flatMap { it.difference(rebootStep.cuboid) }.toSet()
        }
    }
    return poweredOn.sumOf { it.volume() }
}

class Day22 {
    companion object {
        fun part1(rebootSequence: List<RebootStep>): Long {
            val validCubes = Cuboid(xRange = -50L..50L, yRange = -50L..50L, zRange = -50L..50L)
            return rebootReactor(rebootSequence.filter { validCubes.intersects(it.cuboid) }
                .map { it.copy(cuboid = validCubes.intersect(it.cuboid)!!) })
        }

        fun part2(rebootSequence: List<RebootStep>): Long = rebootReactor(rebootSequence)
    }
}

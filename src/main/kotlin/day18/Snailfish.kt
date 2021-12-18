package day18

import org.typemeta.funcj.data.Chr
import org.typemeta.funcj.parser.Combinators.choice
import org.typemeta.funcj.parser.Input
import org.typemeta.funcj.parser.Parser
import org.typemeta.funcj.parser.Ref
import org.typemeta.funcj.parser.Text.chr
import org.typemeta.funcj.parser.Text.ulng
import kotlin.math.ceil
import kotlin.math.floor

sealed interface ExplodeResult

data class Explosion(val left: Long?, val right: Long?) : ExplodeResult

object NoExplosion : ExplodeResult

sealed interface SplitResult

data class SplitToNode(val newNode: SnailfishNode?) : SplitResult

object NoSplit : SplitResult

sealed interface SnailfishNumber {

    fun magnitude(): Long

    fun split(): SplitResult

    fun explode(depth: Int): ExplodeResult

    fun addToRightmostLeaf(value: Long)

    fun addToLeftmostLeaf(value: Long)

    fun deepCopy(): SnailfishNumber

    fun reduced(): SnailfishNumber {
        var explodeResult: ExplodeResult
        var splitResult: SplitResult = NoSplit
        do {
            explodeResult = explode(depth = 0)
            if (explodeResult is Explosion) {
                continue
            }

            splitResult = split()
        } while (explodeResult is Explosion || splitResult is SplitToNode)

        return this
    }

    operator fun plus(addend: SnailfishNumber): SnailfishNumber =
        SnailfishNode(left = this, right = addend)

    companion object {
        fun parse(input: String): SnailfishNumber {
            val numberParser: Ref<Chr, SnailfishNumber> = Parser.ref()
            val leafParser: Parser<Chr, SnailfishNumber> =
                ulng.map { SnailfishLeaf(value = it) }
            val nodeParser: Parser<Chr, SnailfishNumber> =
                chr('[').andR(numberParser).andL(chr(',')).and(numberParser).andL(chr(']'))
                    .map { left, right -> SnailfishNode(left = left, right = right) }
            numberParser.set(choice(leafParser, nodeParser))
            return nodeParser.parse(Input.of(input)).orThrow
        }
    }
}

data class SnailfishNode(var left: SnailfishNumber, var right: SnailfishNumber) : SnailfishNumber {
    override fun magnitude(): Long {
        return 3 * left.magnitude() + 2 * right.magnitude()
    }

    override fun split(): SplitResult {
        val leftSplit = left.split()
        if (leftSplit is SplitToNode) {
            return if (leftSplit.newNode == null) {
                leftSplit
            } else {
                left = leftSplit.newNode
                leftSplit.copy(newNode = null)
            }
        }

        val rightSplit = right.split()
        if (rightSplit is SplitToNode) {
            return if (rightSplit.newNode == null) {
                rightSplit
            } else {
                right = rightSplit.newNode
                rightSplit.copy(newNode = null)
            }
        }

        return NoSplit
    }

    override fun explode(depth: Int): ExplodeResult {
        if (depth == 4) {
            val leftLeaf = left as SnailfishLeaf
            val rightLeaf = right as SnailfishLeaf
            return Explosion(left = leftLeaf.value, right = rightLeaf.value)
        }

        val leftExplodeResult = left.explode(depth = depth + 1)
        if (leftExplodeResult is Explosion) {
            if (depth == 3) {
                left = SnailfishLeaf(value = 0)
            }

            return if (leftExplodeResult.right != null) {
                right.addToLeftmostLeaf(leftExplodeResult.right)
                leftExplodeResult.copy(right = null)
            } else {
                leftExplodeResult
            }
        }

        val rightExplodeResult = right.explode(depth = depth + 1)
        if (rightExplodeResult is Explosion) {
            if (depth == 3) {
                right = SnailfishLeaf(value = 0)
            }

            return if (rightExplodeResult.left != null) {
                left.addToRightmostLeaf(rightExplodeResult.left)
                rightExplodeResult.copy(left = null)
            } else {
                rightExplodeResult
            }
        }

        return NoExplosion
    }

    override fun addToRightmostLeaf(value: Long) {
        right.addToRightmostLeaf(value)
    }

    override fun addToLeftmostLeaf(value: Long) {
        left.addToLeftmostLeaf(value)
    }

    override fun deepCopy(): SnailfishNumber =
        SnailfishNode(left = left.deepCopy(), right = right.deepCopy())

    override fun toString(): String =
        "[$left,$right]"
}

data class SnailfishLeaf(var value: Long) : SnailfishNumber {
    override fun magnitude(): Long {
        return value
    }

    override fun split(): SplitResult {
        return if (value >= 10) {
            val left = SnailfishLeaf(value = floor(value / 2.0).toLong())
            val right = SnailfishLeaf(value = ceil(value / 2.0).toLong())
            val node = SnailfishNode(left = left, right = right)
            SplitToNode(newNode = node)
        } else {
            NoSplit
        }
    }

    override fun explode(depth: Int): ExplodeResult =
        NoExplosion

    override fun addToRightmostLeaf(value: Long) {
        this.value += value
    }

    override fun addToLeftmostLeaf(value: Long) {
        this.value += value
    }

    override fun deepCopy(): SnailfishNumber =
        SnailfishLeaf(value = value)

    override fun toString(): String =
        value.toString()
}

fun List<SnailfishNumber>.sum() =
    this.reduce { acc, snailfishNumber -> (acc + snailfishNumber).reduced() }

fun List<SnailfishNumber>.maxPairwiseMagnitude(): Long {
    return this.maxOf { a ->
        this.filterNot { it == a }.maxOf { b -> (a.deepCopy() + b.deepCopy()).reduced().magnitude() }
    }
}

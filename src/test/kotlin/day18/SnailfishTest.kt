package day18

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class SnailfishTest {

    @Test
    fun parse_returnsBinaryTree() {
        val input = "[[[[1,2],[3,4]],[[5,6],[7,8]]],9]"

        val result = SnailfishNumber.parse(input)

        val expected =
            SnailfishNode(
                left = SnailfishNode(
                    left = SnailfishNode(
                        left = SnailfishNode(
                            left = SnailfishLeaf(value = 1),
                            right = SnailfishLeaf(value = 2)
                        ),
                        right = SnailfishNode(left = SnailfishLeaf(value = 3), right = SnailfishLeaf(value = 4))
                    ),
                    right = SnailfishNode(
                        left = SnailfishNode(
                            left = SnailfishLeaf(value = 5),
                            right = SnailfishLeaf(value = 6)
                        ),
                        right = SnailfishNode(left = SnailfishLeaf(value = 7), right = SnailfishLeaf(value = 8))
                    )
                ),
                right = SnailfishLeaf(value = 9)
            )
        assertEquals(expected, result)
    }

    @Test
    fun reduce() {
        assertEquals(
            SnailfishNumber.parse("[[[[0,9],2],3],4]"),
            SnailfishNumber.parse("[[[[[9,8],1],2],3],4]").reduced()
        )
        assertEquals(
            SnailfishNumber.parse("[7,[6,[5,[7,0]]]]"),
            SnailfishNumber.parse("[7,[6,[5,[4,[3,2]]]]]").reduced()
        )
        assertEquals(
            SnailfishNumber.parse("[[6,[5,[7,0]]],3]"),
            SnailfishNumber.parse("[[6,[5,[4,[3,2]]]],1]").reduced()
        )
        assertEquals(
            SnailfishNumber.parse("[[3,[2,[8,0]]],[9,[5,[7,0]]]]"),
            SnailfishNumber.parse("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]").reduced()
        )
    }

    @Test
    fun add() {
        var addends = listOf(
            SnailfishNumber.parse("[[[[4,3],4],4],[7,[[8,4],9]]]"),
            SnailfishNumber.parse("[1,1]")
        )
        assertEquals(
            SnailfishNumber.parse("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"),
            addends.sum().reduced()
        )
        addends = listOf(
            SnailfishNumber.parse("[1,1]"),
            SnailfishNumber.parse("[2,2]"),
            SnailfishNumber.parse("[3,3]"),
            SnailfishNumber.parse("[4,4]"),
        )
        assertEquals(SnailfishNumber.parse("[[[[1,1],[2,2]],[3,3]],[4,4]]"), addends.sum().reduced())
        addends = listOf(
            SnailfishNumber.parse("[1,1]"),
            SnailfishNumber.parse("[2,2]"),
            SnailfishNumber.parse("[3,3]"),
            SnailfishNumber.parse("[4,4]"),
            SnailfishNumber.parse("[5,5]"),
        )
        assertEquals(SnailfishNumber.parse("[[[[3,0],[5,3]],[4,4]],[5,5]]"), addends.sum().reduced())
        addends = listOf(
            SnailfishNumber.parse("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]"),
            SnailfishNumber.parse("[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]"),
            SnailfishNumber.parse("[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]"),
            SnailfishNumber.parse("[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]"),
            SnailfishNumber.parse("[7,[5,[[3,8],[1,4]]]]"),
            SnailfishNumber.parse("[[2,[2,2]],[8,[8,1]]]"),
            SnailfishNumber.parse("[2,9]"),
            SnailfishNumber.parse("[1,[[[9,3],9],[[9,0],[0,7]]]]"),
            SnailfishNumber.parse("[[[5,[7,4]],7],1]"),
            SnailfishNumber.parse("[[[[4,2],2],6],[8,7]]"),
        )
        assertEquals(
            SnailfishNumber.parse("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"),
            addends.sum().reduced()
        )
    }

    @Test
    fun magnitude() {
        assertEquals(143, SnailfishNumber.parse("[[1,2],[[3,4],5]]").magnitude())
        assertEquals(1384, SnailfishNumber.parse("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]").magnitude())
        assertEquals(445, SnailfishNumber.parse("[[[[1,1],[2,2]],[3,3]],[4,4]]").magnitude())
        assertEquals(791, SnailfishNumber.parse("[[[[3,0],[5,3]],[4,4]],[5,5]]").magnitude())
        assertEquals(1137, SnailfishNumber.parse("[[[[5,0],[7,4]],[5,5]],[6,6]]").magnitude())
        assertEquals(3488, SnailfishNumber.parse("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]").magnitude())

        val addends = listOf(
            SnailfishNumber.parse("[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]"),
            SnailfishNumber.parse("[[[5,[2,8]],4],[5,[[9,9],0]]]"),
            SnailfishNumber.parse("[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]"),
            SnailfishNumber.parse("[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]"),
            SnailfishNumber.parse("[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]"),
            SnailfishNumber.parse("[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]"),
            SnailfishNumber.parse("[[[[5,4],[7,7]],8],[[8,3],8]]"),
            SnailfishNumber.parse("[[9,3],[[9,9],[6,[4,9]]]]"),
            SnailfishNumber.parse("[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]"),
            SnailfishNumber.parse("[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]"),
        )
        assertEquals(4140, addends.sum().magnitude())
    }

    @Test
    fun maxPairwiseMagnitude() {
        val addends = listOf(
            SnailfishNumber.parse("[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]"),
            SnailfishNumber.parse("[[[5,[2,8]],4],[5,[[9,9],0]]]"),
            SnailfishNumber.parse("[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]"),
            SnailfishNumber.parse("[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]"),
            SnailfishNumber.parse("[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]"),
            SnailfishNumber.parse("[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]"),
            SnailfishNumber.parse("[[[[5,4],[7,7]],8],[[8,3],8]]"),
            SnailfishNumber.parse("[[9,3],[[9,9],[6,[4,9]]]]"),
            SnailfishNumber.parse("[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]"),
            SnailfishNumber.parse("[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]"),
        )

        val maximum = addends.maxPairwiseMagnitude()

        assertEquals(3993, maximum)
    }
}
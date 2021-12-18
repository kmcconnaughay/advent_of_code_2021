package day17

import day17.countValidInitialVelocities
import day17.findMaximumAltitude
import day17.parseTargetArea
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class TrickShotTest {

    @Test
    fun findMaximumAltitude() {
        val targetArea = parseTargetArea("target area: x=20..30, y=-10..-5")

        val maximumAltitude = findMaximumAltitude(targetArea)

        assertEquals(45, maximumAltitude)
    }

    @Test
    fun countValidInitialVelocities() {
        val targetArea = parseTargetArea("target area: x=20..30, y=-10..-5")

        val numValidInitialVelocities = countValidInitialVelocities(targetArea)

        assertEquals(112, numValidInitialVelocities)
    }
}
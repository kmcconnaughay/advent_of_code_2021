import kotlin.math.sqrt

/** Returns the nth triangular number. */
fun triangularNumber(n: Int): Int {
    require(n >= 0) { "The given argument, n = $n, was negative" }
    // A triangular number is the sum of the first n natural numbers:
    // t = 1 + 2 + ... + (n - 1) + n
    //
    // Note that t is also equal to the following:
    // t = n + (n - 1) + ... + 2 + 1
    //
    // Add these two representations together element-wise:
    // 2 * t = (1 + n) + (2 + (n - 1)) + ... + ((n - 1) + 2) + (n + 1)
    //
    // Simplify within each parenthesis:
    // 2 * t = (n + 1) + (n + 1) + ... + (n + 1) + (n + 1)
    //
    // Group like terms:
    // 2 * t = n * (n + 1)
    //
    // Divide by two:
    // t = n * (n + 1) / 2
    return n * (n + 1) / 2
}

/**
 * Returns the n such that abs(triangularNumber(inverseTriangularNumber) - t) is minimized. If t is not a triangular
 * number and its two neighboring triangular numbers are equidistant, the higher neighbor is returned.
 */
fun inverseTriangularNumber(t: Int): Int {
    require(t > 0) { "The given argument, t = $t, was not positive" }
    // Start with the formula for a triangular number:
    // t = n * (n + 1) / 2
    // 2 * t = n * (n + 1)
    //
    // The value of 2 * t is between two consecutive squares:
    // n^2 < n * (n + 1) < (n + 1)^2
    // n^2 < 2 * t < (n + 1)^2
    //
    // After taking the square root, it's clear that sqrt(2 * t) is between two consecutive integers, n and n + 1:
    // n < sqrt(2 * t) < n + 1
    //
    // Rounding down thus gives us n:
    // n = floor(sqrt(2 * t))
    return sqrt(2.0 * t).toInt()
}

fun isTriangularNumber(t: Int): Boolean =
    triangularNumber(inverseTriangularNumber(t)) == t

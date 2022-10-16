package com.elian.computeit.feature_tests.presentation.util

fun getRandomPairOfNumbers(min: Int, max: Int): Pair<Int, Int>
{
    val range = IntRange(min, max)

    val first = range.random()
    val second = range.random()

    return first to second
}
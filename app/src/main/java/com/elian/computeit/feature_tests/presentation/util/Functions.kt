package com.elian.computeit.feature_tests.presentation.util

import kotlin.random.Random

private val random = Random(System.currentTimeMillis())

fun getRandomPairOfNumbers(min: Int, max: Int): Pair<Int, Int>
{
    val range = min..max

    val first = range.random(random)
    val second = range.random(random)

    return first to second
}
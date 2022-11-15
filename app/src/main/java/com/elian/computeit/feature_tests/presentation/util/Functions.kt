package com.elian.computeit.feature_tests.presentation.util

import com.elian.computeit.core.domain.models.NumberPair
import kotlin.random.Random

private val random = Random(System.currentTimeMillis())

fun getRandomPairOfNumbers(min: Int, max: Int): NumberPair
{
    val range = min..max

    val first = range.random(random)
    val second = range.random(random)

    return NumberPair(first, second)
}
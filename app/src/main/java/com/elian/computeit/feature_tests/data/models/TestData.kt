package com.elian.computeit.feature_tests.data.models

data class TestData(
    val pairOfNumbers: Pair<Int, Int>,
    val operation: String,
    val insertedResult: Int,
    val expectedResult: Int,
    val millisSinceStart: Long,
)
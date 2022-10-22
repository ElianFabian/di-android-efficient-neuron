package com.elian.computeit.feature_tests.domain.models

data class TestData(
    val pairOfNumbers: Pair<Int, Int>,
    val operation: String,
    val insertedResult: Int,
    val correctResult: Int,
    val millisSinceStart: Long,
)
{
    val isError get() = insertedResult == correctResult
}
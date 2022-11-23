package com.elian.computeit.feature_tests.domain.models

import com.elian.computeit.core.domain.models.TestData
import com.elian.computeit.core.util.extensions.*

data class TestListInfo(
    val testsCompleted: Int,
    val operationsCompleted: Int,
    val correctOperationsCompleted: Int,
    val correctOperationsCompletedPercentage: Float,
    val averageOpm: Float,
    val averageRawOpm: Float,
    val maxOpm: Int,
    val maxRawOpm: Int,
    val opmPerTest: List<Int>,
    val rawOpmPerTest: List<Int>,
)


fun List<TestData>.toTestListInfo() = run()
{
    TestListInfo(
        testsCompleted = size,
        operationsCompleted = operationsCompleted,
        correctOperationsCompleted = correctOperationsCompleted,
        correctOperationsCompletedPercentage = correctOperationsCompletedPercentage,
        averageOpm = averageOpm,
        averageRawOpm = averageRawOpm,
        maxOpm = maxOpm,
        maxRawOpm = maxRawOpm,
        opmPerTest = opmPerTest,
        rawOpmPerTest = rawOpmPerTest,
    )
}
package com.elian.computeit.core.util.extensions

import com.elian.computeit.core.domain.models.TestData
import com.elian.computeit.core.domain.models.TestSessionData

private fun TestSessionData.getValuePerSecond(
    getCountSinceStartFromCondition: (testData: TestData) -> Boolean = { true },
    getValue: (currentSecond: Int, countSinceStart: Int) -> Float,
): Map<Int, Int>
{
    val start = 1
    val end = testTimeInSeconds

    return (start..end).associateWith { currentSecond ->

        val countSinceStart = testDataList.count()
        {
            getCountSinceStartFromCondition(it) && it.millisSinceStart < currentSecond * 1000
        }

        val value = getValue(currentSecond, countSinceStart)

        value.ifNaNReturnZero().toInt()
    }
}

val TestSessionData.rawTpmPerSecond
    get() = getValuePerSecond(
        getValue = { currentSecond, testCountSinceStart ->
            testCountSinceStart / currentSecond.toFloat() * 60
        },
    )

val TestSessionData.tpmPerSecond
    get() = getValuePerSecond(
        getCountSinceStartFromCondition = { !it.isError },
        getValue = { currentSecond, testCountSinceStart ->
            testCountSinceStart / currentSecond.toFloat() * 60
        },
    )

val TestSessionData.rawTpm get() = rawTpmPerSecond.values.lastOrNull() ?: 0
val TestSessionData.tpm get() = tpmPerSecond.values.lastOrNull() ?: 0
val TestSessionData.errorCount get() = testDataList.count { it.isError }
val TestSessionData.maxTpm get() = tpmPerSecond.maxBy { it.value }.value

val List<TestSessionData>.tpmPerSession get() = map { it.tpm }
val List<TestSessionData>.rawTpmPerSession get() = map { it.rawTpm }
val List<TestSessionData>.averageTpm get() = tpmPerSession.average().toFloat()
val List<TestSessionData>.averageRawTpm get() = rawTpmPerSession.average().toFloat()
val List<TestSessionData>.maxTpm get() = tpmPerSession.max()
val List<TestSessionData>.maxRawTpm get() = rawTpmPerSession.max()
val List<TestSessionData>.testsCount get() = sumOf { it.testDataList.size }

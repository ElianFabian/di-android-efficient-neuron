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

val TestSessionData.rawTpm get() = rawTpmPerSecond.values.last()
val TestSessionData.tpm get() = tpmPerSecond.values.last()
val TestSessionData.errorCount get() = testDataList.count { it.isError }
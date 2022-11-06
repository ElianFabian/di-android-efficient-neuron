package com.elian.computeit.core.util.extensions

import com.elian.computeit.feature_tests.data.models.TestData
import com.elian.computeit.feature_tests.data.models.TestSessionData


private fun TestSessionData.getSpeedOverTimeInTPM(
    testCountCondition: (testData: TestData, currentSecond: Int) -> Boolean,
): Map<Int, Int>
{
    val start = 1
    val end = testTimeInSeconds

    return (start..end).associateWith { currentSecond ->

        val testCountSinceStart = testDataList.count { testCountCondition(it, currentSecond) }

        val velocity = testCountSinceStart / currentSecond.toFloat() * 60

        velocity.ifNaNReturnZero().toInt()
    }
}

val TestSessionData.rawSpeedOverTimeInTpm
    get() = getSpeedOverTimeInTPM { testData, currentSecond ->
        testData.millisSinceStart < currentSecond * 1000
    }

val TestSessionData.speedOverTimeInTpm
    get() = getSpeedOverTimeInTPM { testData, currentSecond ->
        testData.isError.not() && testData.millisSinceStart < currentSecond * 1000
    }
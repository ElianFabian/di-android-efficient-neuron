package com.elian.computeit.core.util.extensions

import com.elian.computeit.feature_tests.data.models.TestSessionData

val TestSessionData.speedOverTimeInSeconds
    get(): Map<Int, Float>
    {
        val start = 1
        val end = testTimeInSeconds

        return (start..end).associateWith { currentSecond ->

            val testCountSinceStart = testDataList.count { it.millisSinceStart.fromMillisToSeconds() < currentSecond }

            val velocity = testCountSinceStart / currentSecond.toFloat()

            velocity.ifNaNReturnZero()
        }
    }
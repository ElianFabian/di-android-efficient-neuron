package com.elian.computeit.core.util.extensions

import com.elian.computeit.core.domain.models.OperationData
import com.elian.computeit.core.domain.models.TestData

private fun TestData.getValuePerSecond(
    getCountSinceStartFromCondition: (testData: OperationData) -> Boolean = { true },
    getValue: (currentSecond: Int, countSinceStart: Int) -> Float,
): Map<Int, Int>
{
    val start = 1
    val end = testTimeInSeconds

    return (start..end).associateWith { currentSecond ->

        val countSinceStart = operationDataList.count()
        {
            getCountSinceStartFromCondition(it) && it.millisSinceStart < currentSecond * 1000
        }

        val value = getValue(currentSecond, countSinceStart)

        value.ifNaNReturnZero().toInt()
    }
}

val TestData.rawOpmPerSecond
    get() = getValuePerSecond(
        getValue = { currentSecond, testCountSinceStart ->
            testCountSinceStart / currentSecond.toFloat() * 60
        },
    )

val TestData.opmPerSecond
    get() = getValuePerSecond(
        getCountSinceStartFromCondition = { !it.isError },
        getValue = { currentSecond, testCountSinceStart ->
            testCountSinceStart / currentSecond.toFloat() * 60
        },
    )

val TestData.opm get() = opmPerSecond.values.lastOrNull() ?: 0
val TestData.rawOpm get() = rawOpmPerSecond.values.lastOrNull() ?: 0
val TestData.errorCount get() = operationDataList.count { it.isError }

val List<TestData>.opmPerTest get() = map { it.opm }
val List<TestData>.rawOpmPerTest get() = map { it.rawOpm }
val List<TestData>.averageOpm get() = opmPerTest.average().toFloat().ifNaNReturnZero()
val List<TestData>.averageRawOpm get() = rawOpmPerTest.average().toFloat().ifNaNReturnZero()
val List<TestData>.maxOpm get() = opmPerTest.maxOrNull() ?: 0
val List<TestData>.maxRawOpm get() = rawOpmPerTest.maxOrNull() ?: 0
val List<TestData>.operationsCompleted get() = sumOf { it.operationDataList.size }
val List<TestData>.correctOperationsCompleted get() = sumOf { it.operationDataList.count { data -> !data.isError } }
val List<TestData>.correctOperationsCompletedPercentage get() = (100F * correctOperationsCompleted / operationsCompleted).ifNaNReturnZero()
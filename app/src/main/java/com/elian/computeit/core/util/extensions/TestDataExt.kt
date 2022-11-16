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

        val countSinceStart = testDataList.count()
        {
            getCountSinceStartFromCondition(it) && it.millisSinceStart < currentSecond * 1000
        }

        val value = getValue(currentSecond, countSinceStart)

        value.ifNaNReturnZero().toInt()
    }
}

val TestData.rawTpmPerSecond
    get() = getValuePerSecond(
        getValue = { currentSecond, testCountSinceStart ->
            testCountSinceStart / currentSecond.toFloat() * 60
        },
    )

val TestData.tpmPerSecond
    get() = getValuePerSecond(
        getCountSinceStartFromCondition = { !it.isError },
        getValue = { currentSecond, testCountSinceStart ->
            testCountSinceStart / currentSecond.toFloat() * 60
        },
    )

val TestData.rawTpm get() = rawTpmPerSecond.values.lastOrNull() ?: 0
val TestData.tpm get() = tpmPerSecond.values.lastOrNull() ?: 0
val TestData.errorCount get() = testDataList.count { it.isError }

val List<TestData>.tpmPerTest get() = map { it.tpm }
val List<TestData>.rawTpmPerTest get() = map { it.rawTpm }
val List<TestData>.averageTpm get() = tpmPerTest.average().toFloat()
val List<TestData>.averageRawTpm get() = rawTpmPerTest.average().toFloat()
val List<TestData>.maxTpm get() = tpmPerTest.maxOrNull() ?: 0
val List<TestData>.maxRawTpm get() = rawTpmPerTest.maxOrNull() ?: 0
val List<TestData>.operationsCompleted get() = sumOf { it.testDataList.size }
val List<TestData>.correctOperationsCompleted
    get() = sumOf {
        it.testDataList.count { operationData -> !operationData.isError }
    }

package com.elian.computeit.core.data

import com.elian.computeit.core.domain.models.OperationData
import com.elian.computeit.core.domain.models.TestData
import com.elian.computeit.core.util.constants.defaultDateFormat
import com.elian.computeit.core.util.constants.secondsToDHHMMSS
import com.elian.computeit.core.util.extensions.getValuePerSecond
import com.elian.computeit.core.util.extensions.ifNaNReturnZero
import com.elian.computeit.core.util.extensions.isError
import com.elian.computeit.core.util.extensions.result
import com.elian.computeit.feature_tests.domain.model.OperationInfo
import com.elian.computeit.feature_tests.domain.model.TestInfo
import com.elian.computeit.feature_tests.domain.model.TestListInfo
import java.util.*

fun OperationData.toOperationInfo() = OperationInfo(
	pairOfNumbers = pairOfNumbers,
	operationSymbol = Operation.nameToSymbol[operationName]!!,
	result = result,
	insertedResult = insertedResult,
)

fun TestData.toTestInfo(): TestInfo
{
	val opmPerSecond = getValuePerSecond(
		getCountSinceStartFromCondition = { !it.isError },
		getValue = { currentSecond, testCountSinceStart ->
			testCountSinceStart / currentSecond.toFloat() * 60
		},
	)
	val rawOpmPerSecond = getValuePerSecond(
		getValue = { currentSecond, testCountSinceStart ->
			testCountSinceStart / currentSecond.toFloat() * 60
		},
	)

	val maxOpm = opmPerSecond.values.maxOrNull() ?: 0
	val maxRawOpm = rawOpmPerSecond.values.maxOrNull() ?: 0
	val minOpm = opmPerSecond.values.minOrNull() ?: 0
	val minRawOpm = rawOpmPerSecond.values.minOrNull() ?: 0

	return TestInfo(
		date = defaultDateFormat.format(Date(dateUnix)),
		opm = opmPerSecond.values.lastOrNull() ?: 0,
		rawOpm = rawOpmPerSecond.values.lastOrNull() ?: 0,
		maxOpm = opmPerSecond.values.maxOfOrNull { it } ?: 0,
		maxRawOpm = rawOpmPerSecond.values.maxOfOrNull { it } ?: 0,
		timeInSeconds = "$timeInSeconds s",
		operationCount = listOfOperationData.size,
		errorCount = listOfOperationData.count { it.isError },
		opmPerSecond = opmPerSecond,
		rawOpmPerSecond = rawOpmPerSecond,
		errorsAtSecond = listOfOperationData.filter { it.millisSinceStart >= 1000 && it.isError }.map { it.millisSinceStart / 1000F },
		errorsYValue = (maxOf(maxOpm, maxRawOpm) + minOf(minOpm, minRawOpm)) / 2,
		listOfFailedOperationInfo = listOfOperationData.filter { it.isError }.map { it.toOperationInfo() },
	)
}

fun List<TestData>.toTestListInfo(): TestListInfo
{
	val totalTimeInSeconds = sumOf { it.timeInSeconds }

	val operationsCompleted = sumOf { it.listOfOperationData.size }
	val correctOperationsCompleted = sumOf { it.listOfOperationData.count { data -> !data.isError } }

	val opmPerTest = map()
	{
		(it.listOfOperationData.count { operation -> !operation.isError } / it.timeInSeconds.toFloat() * 60F).ifNaNReturnZero().toInt()
	}
	val rawOpmPerTest = map()
	{
		(it.listOfOperationData.size / it.timeInSeconds.toFloat() * 60F).ifNaNReturnZero().toInt()
	}

	return TestListInfo(
		testsCompleted = size,
		totalTime = secondsToDHHMMSS(totalTimeInSeconds),
		operationsCompleted = operationsCompleted,
		correctOperationsCompleted = correctOperationsCompleted,
		correctOperationsCompletedPercentage = (100F * correctOperationsCompleted / operationsCompleted).ifNaNReturnZero(),
		averageOpm = opmPerTest.average().toFloat().ifNaNReturnZero(),
		averageRawOpm = rawOpmPerTest.average().toFloat().ifNaNReturnZero(),
		maxOpm = opmPerTest.maxOrNull() ?: 0,
		maxRawOpm = rawOpmPerTest.maxOrNull() ?: 0,
		opmPerTest = opmPerTest,
		rawOpmPerTest = rawOpmPerTest,
		listOfTestInfo = this.map { it.toTestInfo() },
	)
}
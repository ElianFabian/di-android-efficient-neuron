package com.elian.computeit.core.data

import com.elian.computeit.core.domain.models.OperationData
import com.elian.computeit.core.domain.models.TestData
import com.elian.computeit.core.util.constants.defaultFullDateFormat
import com.elian.computeit.core.util.constants.secondsToDhhmmss
import com.elian.computeit.core.util.extensions.getValuePerSecond
import com.elian.computeit.core.util.extensions.ifNaNReturnZero
import com.elian.computeit.core.util.extensions.isError
import com.elian.computeit.core.util.extensions.result
import com.elian.computeit.feature_tests.domain.model.*
import java.util.*
import kotlin.math.ceil

fun OperationData.toOperationInfo() = OperationInfo(
	pairOfNumbers = pairOfNumbers,
	operationSymbol = Operation.nameToSymbol[operationName]!!,
	result = result,
	insertedResult = insertedResult,
)

fun TestData.toTestInfo(): TestInfo
{
	val opmPerSecond = getValuePerSecond(
		countSinceStartCondition = { !it.isError },
		getValue = { currentSecond, testCountSinceStart ->
			testCountSinceStart / currentSecond.toFloat() * 60
		},
	)
	val rawOpmPerSecond = getValuePerSecond(
		getValue = { currentSecond, testCountSinceStart ->
			testCountSinceStart / currentSecond.toFloat() * 60
		},
	)

	val minOpm = opmPerSecond.values.minOrNull() ?: 0
	val minRawOpm = rawOpmPerSecond.values.minOrNull() ?: 0
	val maxOpm = opmPerSecond.values.maxOrNull() ?: 0
	val maxRawOpm = rawOpmPerSecond.values.maxOrNull() ?: 0

	val errorYValuePercentage = 0.5F

	return TestInfo(
		chartInfo = TestChartInfo(
			opmPerSecond = opmPerSecond,
			rawOpmPerSecond = rawOpmPerSecond,
			errorsAtSecond = listOfOperationData.filter { it.millisSinceStart >= 1000 && it.isError }.map { it.millisSinceStart / 1000F },
			errorsYValue = (errorYValuePercentage * (minOf(minOpm, minRawOpm) + maxOf(maxOpm, maxRawOpm))).toInt(),
		),
		statsInfo = TestStatsInfo(
			date = defaultFullDateFormat.format(Date(dateUnix)),
			opm = opmPerSecond.values.lastOrNull() ?: 0,
			rawOpm = rawOpmPerSecond.values.lastOrNull() ?: 0,
			maxOpm = opmPerSecond.values.maxOfOrNull { it } ?: 0,
			maxRawOpm = rawOpmPerSecond.values.maxOfOrNull { it } ?: 0,
			timeInSeconds = timeInSeconds,
			operationCount = listOfOperationData.size,
			errorCount = listOfOperationData.count { it.isError },
		),
		listOfFailedOperationInfo = listOfOperationData.filter { it.isError }.map { it.toOperationInfo() },
	)
}

fun List<TestData>.toTestListInfo(): TestListInfo
{
	val totalTimeInSeconds = sumOf { it.timeInSeconds }
	val operationsCompleted = sumOf { it.listOfOperationData.size }
	val correctOperationsCompleted = sumOf { it.listOfOperationData.count { data -> !data.isError } }

	val listOfTestInfo = map { it.toTestInfo() }
	val opmPerTest = listOfTestInfo.map { it.statsInfo.opm }
	val rawOpmPerTest = listOfTestInfo.map { it.statsInfo.rawOpm }

	val minOpm = opmPerTest.minOrNull() ?: 0
	val maxOpm = opmPerTest.maxOrNull() ?: 0

	val maxAndMinOpmDifference = maxOpm - minOpm

	val defaultRangeLength = getBarRangeLength(
		barCount = 5,
		maxAndMinOpmDifference = maxAndMinOpmDifference,
	)
	val testsPerSpeedRange = getTestsPerSpeedRange(
		opmPerTest = opmPerTest,
		speedRangeLength = defaultRangeLength,
	)

	return TestListInfo(
		historyInfo = TestHistoryInfo(
			opmPerTest = opmPerTest,
			rawOpmPerTest = rawOpmPerTest,
			listOfTestInfo = listOfTestInfo,
		),
		speedHistogramInfo = SpeedHistogramInfo(
			speedRangeLength = defaultRangeLength,
			testsPerSpeedRange = testsPerSpeedRange,
			speedRangeLengthMinValue = 1,
			speedRangeLengthMaxValue = maxAndMinOpmDifference,
			isSliderVisible = (maxAndMinOpmDifference > 1) && (minOpm != maxOpm),
		),
		statsInfo = TestListStatsInfo(
			testsCompleted = size,
			totalTime = secondsToDhhmmss(totalTimeInSeconds),
			operationsCompleted = operationsCompleted,
			correctOperationsCompleted = correctOperationsCompleted,
			correctOperationsCompletedPercentage = (100F * correctOperationsCompleted / operationsCompleted).ifNaNReturnZero(),
			averageOpm = opmPerTest.average().toFloat().ifNaNReturnZero(),
			averageRawOpm = rawOpmPerTest.average().ifNaNReturnZero().toFloat(),
			minOpm = minOpm,
			maxOpm = maxOpm,
			maxRawOpm = rawOpmPerTest.maxOrNull() ?: 0,
		),
	)
}

fun List<TestData>.toTestsPerSpeedRange(rangeLength: Int): List<Int>
{
	val opmPerTest = map()
	{
		val correctOperations = it.listOfOperationData.count { operation -> !operation.isError }
		val opm = (correctOperations / it.timeInSeconds.toFloat() * 60).ifNaNReturnZero().toInt()

		opm
	}

	return getTestsPerSpeedRange(
		opmPerTest = opmPerTest,
		speedRangeLength = rangeLength,
	)
}


private fun getTestsPerSpeedRange(
	opmPerTest: List<Int>,
	speedRangeLength: Int,
): List<Int>
{
	val minOpm = opmPerTest.minOrNull() ?: 0
	val maxOpm = opmPerTest.maxOrNull() ?: 0

	val rangeCount = if (maxOpm != 0)
	{
		((maxOpm - minOpm) / speedRangeLength.toDouble() + 1).ifNaNReturnZero().toInt()
	}
	else 0

	val speedRanges = IntArray(rangeCount) { 0 }

	opmPerTest.forEach()
	{
		val testRangePosition = ((it - minOpm) / speedRangeLength.toDouble()).ifNaNReturnZero().toInt()

		speedRanges[testRangePosition]++
	}

	return speedRanges.toList()
}

private fun getBarRangeLength(barCount: Int, maxAndMinOpmDifference: Int): Int
{
	return ceil(maxAndMinOpmDifference / barCount.toDouble()).toInt() + 1
}
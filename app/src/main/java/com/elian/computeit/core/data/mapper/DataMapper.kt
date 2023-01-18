package com.elian.computeit.core.data.mapper

import com.elian.computeit.core.data.model.OperationData
import com.elian.computeit.core.data.model.TestData
import com.elian.computeit.core.domain.models.*
import com.elian.computeit.core.util.constants.defaultFullDateFormat
import com.elian.computeit.core.util.constants.secondsToDhhmmss
import com.elian.computeit.core.util.extensions.getListOfValuePerSecond
import com.elian.computeit.core.util.extensions.ifNaNReturnZero
import com.elian.computeit.core.util.extensions.isError
import com.elian.computeit.core.util.extensions.result
import java.util.*
import kotlin.math.ceil

fun OperationData.toOperationInfo() = OperationInfo(
	pairOfNumbers = pairOfNumbers,
	operationSymbol = OperationType.nameToSymbol[operationName]!!,
	result = result,
	insertedResult = insertedResult,
)

fun TestData.toTestInfo(): TestInfo
{
	val listOfOpmPerSecond = this.getListOfValuePerSecond(
		countCondition = { !it.isError },
		getValue = { currentSecond, currentOperationCount ->
			currentOperationCount / currentSecond.toFloat() * 60
		},
	)
	val listOfRawOpmPerSecond = this.getListOfValuePerSecond(
		getValue = { currentSecond, currentOperationCount ->
			currentOperationCount / currentSecond.toFloat() * 60
		},
	)

	val minOpm = listOfOpmPerSecond.values.minOrNull() ?: 0
	val minRawOpm = listOfRawOpmPerSecond.values.minOrNull() ?: 0
	val maxOpm = listOfOpmPerSecond.values.maxOrNull() ?: 0
	val maxRawOpm = listOfRawOpmPerSecond.values.maxOrNull() ?: 0

	val errorYValuePercentage = 0.5F

	return TestInfo(
		chartInfo = TestChartInfo(
			listOfOpmPerSecond = listOfOpmPerSecond,
			listOfRawOpmPerSecond = listOfRawOpmPerSecond,
			errorSeconds = listOfOperationData.filter { it.millisSinceStart >= 1000 && it.isError }.map { it.millisSinceStart / 1000F },
			errorsYValue = (errorYValuePercentage * (minOf(minOpm, minRawOpm) + maxOf(maxOpm, maxRawOpm))).toInt(),
		),
		statsInfo = TestStatsInfo(
			date = defaultFullDateFormat.format(Date(dateUnix)),
			opm = listOfOpmPerSecond.values.lastOrNull() ?: 0,
			rawOpm = listOfRawOpmPerSecond.values.lastOrNull() ?: 0,
			minOpm = minOpm,
			maxOpm = maxOpm,
			minRawOpm = minRawOpm,
			maxRawOpm = maxRawOpm,
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
	val listOfOpmPerTest = listOfTestInfo.map { it.statsInfo.opm }
	val listOfRawOpmPerTest = listOfTestInfo.map { it.statsInfo.rawOpm }

	val minOpm = listOfOpmPerTest.minOrNull() ?: 0
	val maxOpm = listOfOpmPerTest.maxOrNull() ?: 0

	val maxAndMinOpmDifference = maxOpm - minOpm

	val defaultRangeLength = getBarRangeLength(
		barCount = 5,
		maxAndMinOpmDifference = maxAndMinOpmDifference,
	)
	val testsPerSpeedRange = getListOfTestsPerSpeedRange(
		listOfOpmPerTest = listOfOpmPerTest,
		speedRangeLength = defaultRangeLength,
	)

	return TestListInfo(
		historyInfo = TestHistoryInfo(
			listOfOpmPerTest = listOfOpmPerTest,
			listOfRawOpmPerTest = listOfRawOpmPerTest,
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
			testsCompleted = this.size,
			formattedTotalTime = secondsToDhhmmss(totalTimeInSeconds),
			operationsCompleted = operationsCompleted,
			correctOperationsCompleted = correctOperationsCompleted,
			correctOperationsCompletedPercentage = (100F * correctOperationsCompleted / operationsCompleted).ifNaNReturnZero(),
			averageOpm = listOfOpmPerTest.average().toFloat().ifNaNReturnZero(),
			averageRawOpm = listOfRawOpmPerTest.average().ifNaNReturnZero().toFloat(),
			minOpm = minOpm,
			maxOpm = maxOpm,
			minRawOpm = listOfRawOpmPerTest.minOrNull() ?: 0,
			maxRawOpm = listOfRawOpmPerTest.maxOrNull() ?: 0,
		),
	)
}

fun List<TestData>.toListOfTestsPerSpeedRange(rangeLength: Int): List<Int>
{
	val listOfOpmPerTest = map()
	{
		val correctOperations = it.listOfOperationData.count { operation -> !operation.isError }
		val testOpm = (correctOperations / it.timeInSeconds.toFloat() * 60).ifNaNReturnZero().toInt()

		testOpm
	}

	return getListOfTestsPerSpeedRange(
		listOfOpmPerTest = listOfOpmPerTest,
		speedRangeLength = rangeLength,
	)
}


private fun getListOfTestsPerSpeedRange(
	listOfOpmPerTest: List<Int>,
	speedRangeLength: Int,
): List<Int>
{
	val minOpm = listOfOpmPerTest.minOrNull() ?: 0
	val maxOpm = listOfOpmPerTest.maxOrNull() ?: 0

	val rangeCount = if (maxOpm != 0)
	{
		((maxOpm - minOpm) / speedRangeLength.toDouble() + 1).ifNaNReturnZero().toInt()
	}
	else 0

	val listOfTestsPerSpeedRange = IntArray(rangeCount)

	listOfOpmPerTest.forEach { testOpm ->

		val testRangePosition = ((testOpm - minOpm) / speedRangeLength.toDouble()).ifNaNReturnZero().toInt()

		listOfTestsPerSpeedRange[testRangePosition]++
	}

	return listOfTestsPerSpeedRange.toList()
}

private fun getBarRangeLength(barCount: Int, maxAndMinOpmDifference: Int): Int
{
	return ceil(maxAndMinOpmDifference / barCount.toDouble()).toInt() + 1
}
package com.elian.computeit.core.data

import com.elian.computeit.core.domain.models.OperationData
import com.elian.computeit.core.domain.models.TestData
import com.elian.computeit.core.util.constants.defaultFullDateFormat
import com.elian.computeit.core.util.constants.secondsToDHHMMSS
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

	val minOpm = opmPerTest.minOrNull()
	val maxOpm = opmPerTest.maxOrNull()

	val normalizedMinOpm = minOpm ?: 0
	val normalizedMaxOpm = maxOpm ?: 0

	val defaultRangeLength = ceil((normalizedMaxOpm - normalizedMinOpm) / 2F).toInt()

	val testsPerSpeedRange = getTestsPerSpeedRanges(
		opmPerTest = opmPerTest,
		speedRangeLength = defaultRangeLength,
	)

	val isSliderVisible = normalizedMaxOpm - normalizedMinOpm > 1 && minOpm != maxOpm

	return TestListInfo(
		historyInfo = TestHistoryInfo(
			opmPerTest = opmPerTest,
			rawOpmPerTest = rawOpmPerTest,
			listOfTestInfo = listOfTestInfo,
		),
		speedHistogramInfo = SpeedHistogramInfo(
			speedRangeLength = defaultRangeLength,
			testsPerSpeedRange = testsPerSpeedRange.toList(),
			speedRangeLengthMinValue = 1,
			speedRangeLengthMaxValue = normalizedMaxOpm - normalizedMinOpm,
			isSliderVisible = isSliderVisible,
		),
		statsInfo = TestListStatsInfo(
			testsCompleted = size,
			totalTime = secondsToDHHMMSS(totalTimeInSeconds),
			operationsCompleted = operationsCompleted,
			correctOperationsCompleted = correctOperationsCompleted,
			correctOperationsCompletedPercentage = (100F * correctOperationsCompleted / operationsCompleted).ifNaNReturnZero(),
			averageOpm = opmPerTest.average().toFloat().ifNaNReturnZero(),
			averageRawOpm = rawOpmPerTest.average().toFloat().ifNaNReturnZero(),
			minOpm = normalizedMinOpm,
			maxOpm = normalizedMaxOpm,
			maxRawOpm = rawOpmPerTest.maxOrNull() ?: 0,
		),
	)
}

fun List<TestData>.toSpeedHistogramInfo(rangeLength: Int): SpeedHistogramInfo
{
	val opmPerTest = map()
	{
		val correctOperations = it.listOfOperationData.count { operation -> !operation.isError }

		val opm = (correctOperations / it.timeInSeconds.toFloat() * 60).ifNaNReturnZero().toInt()

		opm
	}

	val minOpm = opmPerTest.minOrNull() ?: 0
	val maxOpm = opmPerTest.maxOrNull() ?: 0

	val testsPerSpeedRange = getTestsPerSpeedRanges(
		opmPerTest = opmPerTest,
		speedRangeLength = rangeLength,
	)

	return SpeedHistogramInfo(
		speedRangeLength = rangeLength,
		testsPerSpeedRange = testsPerSpeedRange,
		speedRangeLengthMinValue = 1,
		speedRangeLengthMaxValue = minOpm - maxOpm,
		isSliderVisible = true,
	)
}


private fun getTestsPerSpeedRanges(
	opmPerTest: List<Int>,
	speedRangeLength: Int,
): List<Int>
{
	val minOpm = opmPerTest.minOrNull() ?: 0
	val maxOpm = opmPerTest.maxOrNull() ?: 0

	val rangeCount = if (maxOpm != 0) (maxOpm - minOpm) / speedRangeLength + 1 else 0
	val speedRanges = Array(rangeCount) { 0 }

	opmPerTest.forEach()
	{
		val position = (it - minOpm) / speedRangeLength
		speedRanges.getOrNull(position)?.also { speedRanges[position]++ }
	}

	return speedRanges.toList()
}
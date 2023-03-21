package com.elian.computeit.core.domain.models

data class TestListInfo(
	val historyInfo: TestHistoryInfo,
	val speedHistogramInfo: SpeedHistogramInfo,
	val statsInfo: TestListStatsInfo,
)


data class TestHistoryInfo(
	val listOfOpmPerTest: List<Int>,
	val listOfRawOpmPerTest: List<Int>,
	val listOfTestInfo: List<TestInfo>,
)

data class SpeedHistogramInfo(
	val speedRangeLength: Int,
	val testsPerSpeedRange: List<Int>,
	val speedRangeLengthValueFrom: Int,
	val speedRangeLengthValueTo: Int,
	val isSliderVisible: Boolean,
)

data class TestListStatsInfo(
	val testsCompletedCount: Int,
	val testsCompletedWithoutErrorsCount: Int,
	val testsCompletedWithoutErrorsPercentage: Float,
	val formattedTotalTime: String,
	val operationsCompleted: Int,
	val correctOperationsCompletedCount: Int,
	val correctOperationsCompletedPercentage: Float,
	val averageOpm: Float,
	val averageRawOpm: Float,
	val minOpm: Int,
	val maxOpm: Int,
	val minRawOpm: Int,
	val maxRawOpm: Int,
)
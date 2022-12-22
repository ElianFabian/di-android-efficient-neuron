package com.elian.computeit.feature_tests.domain.model

data class TestListInfo(
	val historyInfo: TestHistoryInfo,
	val speedHistogramInfo: SpeedHistogramInfo,
	val statsInfo: TestListStatsInfo,
)


data class TestHistoryInfo(
	val opmPerTest: List<Int>,
	val rawOpmPerTest: List<Int>,
	val listOfTestInfo: List<TestInfo>,
)

data class SpeedHistogramInfo(
	val speedRangeLength: Int,
	val testsPerSpeedRange: List<Int>,
	val speedRangeLengthMinValue: Int,
	val speedRangeLengthMaxValue: Int,
	val isSliderVisible: Boolean,
)

data class TestListStatsInfo(
	val testsCompleted: Int,
	val totalTime: String,
	val operationsCompleted: Int,
	val correctOperationsCompleted: Int,
	val correctOperationsCompletedPercentage: Float,
	val averageOpm: Float,
	val averageRawOpm: Float,
	val minOpm: Int,
	val maxOpm: Int,
	val maxRawOpm: Int,
)
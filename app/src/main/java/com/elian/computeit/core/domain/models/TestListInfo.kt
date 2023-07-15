package com.elian.computeit.core.domain.models

data class TestListInfo(
	val listOfOpmPerTest: List<Float>,
	val listOfRawOpmPerTest: List<Float>,
	val listOfTestInfo: List<TestInfo>,

	val speedRangeLength: Int,
	val testsPerSpeedRange: List<Int>,
	val speedRangeLengthValueFrom: Int,
	val speedRangeLengthValueTo: Int,

	val testsCompletedCount: Int,
	val testsCompletedWithoutErrorsCount: Int,
	val testsCompletedWithoutErrorsPercentage: Float,
	val formattedTotalTime: String,
	val operationsCompleted: Int,
	val correctOperationsCompletedCount: Int,
	val correctOperationsCompletedPercentage: Float,
	val averageOpm: Float,
	val averageRawOpm: Float,
	val minOpm: Float,
	val maxOpm: Float,
	val minRawOpm: Float,
	val maxRawOpm: Float,
)
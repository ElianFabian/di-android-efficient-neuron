package com.elian.computeit.feature_tests.domain.model

data class TestListInfo(
	val testsCompleted: Int,
	val totalTime: String,
	val operationsCompleted: Int,
	val correctOperationsCompleted: Int,
	val correctOperationsCompletedPercentage: Float,
	val averageOpm: Float,
	val averageRawOpm: Float,
	val maxOpm: Int,
	val maxRawOpm: Int,
	val opmPerTest: List<Int>,
	val rawOpmPerTest: List<Int>,
	val listOfTestInfo: List<TestInfo>,
)
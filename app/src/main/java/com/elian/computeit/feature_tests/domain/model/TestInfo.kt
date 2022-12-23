package com.elian.computeit.feature_tests.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestInfo(
	val chartInfo: TestChartInfo,
	val statsInfo: TestStatsInfo,
	val listOfFailedOperationInfo: List<OperationInfo>,
) : Parcelable

@Parcelize
data class TestChartInfo(
	val opmPerSecond: Map<Int, Int>,
	val errorsAtSecond: List<Float>,
	val errorsYValue: Int,
	val rawOpmPerSecond: Map<Int, Int>,
) : Parcelable

@Parcelize
data class TestStatsInfo(
	val date: String,
	val opm: Int,
	val rawOpm: Int,
	val maxOpm: Int,
	val maxRawOpm: Int,
	val timeInSeconds: Int,
	val operationCount: Int,
	val errorCount: Int,
) : Parcelable
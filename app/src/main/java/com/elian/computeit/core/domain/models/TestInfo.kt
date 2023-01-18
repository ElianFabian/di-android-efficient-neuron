package com.elian.computeit.core.domain.models

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
	val listOfOpmPerSecond: Map<Int, Int>,
	val listOfRawOpmPerSecond: Map<Int, Int>,
	val errorSeconds: List<Float>,
	val errorsYValue: Int,
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
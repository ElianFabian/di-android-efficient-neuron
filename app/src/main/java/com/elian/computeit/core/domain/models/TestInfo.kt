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
	val listOfOpmPerSecond: Map<Int, Float>,
	val listOfRawOpmPerSecond: Map<Int, Float>,
	val errorSeconds: List<Float>,
	val errorsYValue: Int,
) : Parcelable

@Parcelize
data class TestStatsInfo(
	val date: String,
	val opm: Float,
	val rawOpm: Float,
	val minOpm: Float,
	val maxOpm: Float,
	val minRawOpm: Float,
	val maxRawOpm: Float,
	val timeInSeconds: Int,
	val operationCount: Int,
	val errorCount: Int,
) : Parcelable
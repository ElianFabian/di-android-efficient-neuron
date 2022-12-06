package com.elian.computeit.feature_tests.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestInfo(
	val opm: Int,
	val rawOpm: Int,
	val maxOpm: Int,
	val maxRawOpm: Int,
	val timeInSeconds: String,
	val operationCount: Int,
	val errorCount: Int,
	val opmPerSecond: Map<Int, Int>,
	val errorsAtSecond: List<Float>,
	val errorsYValue: Int,
	val rawOpmPerSecond: Map<Int, Int>,
	val listOfFailedOperationInfo: List<OperationInfo>,
) : Parcelable
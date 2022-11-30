package com.elian.computeit.core.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OperationData(
	val pairOfNumbers: NumberPair = NumberPair(),
	val operationName: String = "",
	val insertedResult: Int = 0,
	val expectedResult: Int = 0,
	val millisSinceStart: Long = 0,
) : Parcelable
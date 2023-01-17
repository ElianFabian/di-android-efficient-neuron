package com.elian.computeit.core.data.model

import android.os.Parcelable
import com.elian.computeit.core.domain.models.NumberPair
import kotlinx.parcelize.Parcelize

@Parcelize
data class OperationData(
	val pairOfNumbers: NumberPair = NumberPair(),
	val operationName: String = "",
	val insertedResult: Int = 0,
	val millisSinceStart: Long = 0,
) : Parcelable
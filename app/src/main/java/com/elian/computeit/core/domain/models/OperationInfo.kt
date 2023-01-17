package com.elian.computeit.core.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OperationInfo(
	val pairOfNumbers: NumberPair,
	val operationSymbol: String,
	val result: Int,
	val insertedResult: Int,
) : Parcelable
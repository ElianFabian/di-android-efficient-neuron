package com.elian.computeit.feature_tests.domain.model

import android.os.Parcelable
import com.elian.computeit.core.domain.models.NumberPair
import kotlinx.parcelize.Parcelize

@Parcelize
data class OperationInfo(
	val pairOfNumbers: NumberPair,
	val operationSymbol: String,
	val result: Int,
	val insertedResult: Int,
) : Parcelable
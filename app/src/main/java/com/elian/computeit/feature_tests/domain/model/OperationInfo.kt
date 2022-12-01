package com.elian.computeit.feature_tests.domain.model

import android.os.Parcelable
import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.domain.models.NumberPair
import com.elian.computeit.core.domain.models.OperationData
import kotlinx.parcelize.Parcelize

@Parcelize
data class OperationInfo(
	val pairOfNumbers: NumberPair,
	val operationSymbol: String,
	val expectedResult: Int,
	val insertedResult: Int,
) : Parcelable


fun OperationData.toOperationInfo() = run()
{
	OperationInfo(
		pairOfNumbers = pairOfNumbers,
		operationSymbol = Operation.nameToSymbol[operationName]!!,
		expectedResult = expectedResult,
		insertedResult = insertedResult
	)
}
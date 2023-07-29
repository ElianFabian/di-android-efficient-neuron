package com.elian.computeit.feature_tests.presentation.test

import com.elian.computeit.core.domain.models.NumberPair
import com.elian.computeit.core.domain.models.OperationType
import com.elian.computeit.core.domain.models.Range

data class TestState(
	val insertedResult: Int = 0,
	val pairOfNumbers: NumberPair? = null,
	val operation: OperationType,
	val totalTimeInMillis: Long,
	val range: Range,
)

val TestState.testType: TestType
	get() = when (totalTimeInMillis) {
		0L   -> TestType.INFINITE
		else -> TestType.FINITE
	}

enum class TestType {
	FINITE,
	INFINITE,
}
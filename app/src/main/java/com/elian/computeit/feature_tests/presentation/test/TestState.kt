package com.elian.computeit.feature_tests.presentation.test

import com.elian.computeit.core.domain.models.NumberPair

data class TestState(
	val insertedResult: Int = 0,
	val operationSymbol: String,
	val pairOfNumbers: NumberPair? = null,
)

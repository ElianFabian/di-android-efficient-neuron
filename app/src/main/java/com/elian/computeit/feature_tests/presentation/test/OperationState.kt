package com.elian.computeit.feature_tests.presentation.test

import com.elian.computeit.core.domain.models.NumberPair

data class OperationState(
	val pairOfNumbers: NumberPair = NumberPair(),
	val symbol: String = "",
)

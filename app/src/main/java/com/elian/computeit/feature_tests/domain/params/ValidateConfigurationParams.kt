package com.elian.computeit.feature_tests.domain.params

import com.elian.computeit.core.data.Operation

data class ValidateConfigurationParams(
	val operation: Operation,
	val start: Int?,
	val end: Int?,
	val time: Int?,
)

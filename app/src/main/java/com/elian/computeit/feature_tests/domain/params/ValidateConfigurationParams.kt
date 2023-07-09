package com.elian.computeit.feature_tests.domain.params

import com.elian.computeit.core.domain.models.OperationType

data class ValidateConfigurationParams(
	val operation: OperationType,
	val startOfRange: Int?,
	val endOfRange: Int?,
	val timeInSeconds: Int?,
)

package com.elian.computeit.feature_tests.presentation.test_configuration

import com.elian.computeit.core.domain.errors.NumericFieldError
import com.elian.computeit.core.domain.models.OperationType

data class TestConfigurationState(
	val selectedOperation: OperationType = OperationType.Addition,
	val startOfRange: Int? = null,
	val startOfRangeError: NumericFieldError? = null,
	val endOfRange: Int? = null,
	val endOfRangeError: NumericFieldError? = null,
	val timeInSeconds: Int? = null,
	val timeError: NumericFieldError? = null,
)

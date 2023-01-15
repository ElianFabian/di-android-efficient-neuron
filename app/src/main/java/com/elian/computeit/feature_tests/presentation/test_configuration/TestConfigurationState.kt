package com.elian.computeit.feature_tests.presentation.test_configuration

import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.util.Error

data class TestConfigurationState(
	val selectedOperation: Operation = Operation.Addition,
	val startOfRange: Int? = null,
	val startOfRangeError: Error? = null,
	val endOfRange: Int? = null,
	val endOfRangeError: Error? = null,
	val time: Int? = null,
	val timeError: Error? = null,
)

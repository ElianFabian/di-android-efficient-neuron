package com.elian.computeit.feature_tests.presentation.test_configuration

import com.elian.computeit.core.domain.models.OperationType

sealed interface TestConfigurationAction {
	data class SelectOperation(val operationType: OperationType) : TestConfigurationAction
	data class EnterStartOfRange(val value: Int?) : TestConfigurationAction
	data class EnterEndOfRange(val value: Int?) : TestConfigurationAction
	data class EnterTime(val timeInSeconds: Int?) : TestConfigurationAction
	object StartTest : TestConfigurationAction
}
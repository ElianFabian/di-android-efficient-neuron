package com.elian.computeit.feature_tests.presentation.test_configuration

sealed interface TestConfigurationAction
{
	data class SelectOperationType(val symbol: String) : TestConfigurationAction
	data class EnterStartOfRange(val value: Int?) : TestConfigurationAction
	data class EnterEndOfRange(val value: Int?) : TestConfigurationAction
	data class EnterTime(val value: Int?) : TestConfigurationAction
	object StartTest : TestConfigurationAction
}
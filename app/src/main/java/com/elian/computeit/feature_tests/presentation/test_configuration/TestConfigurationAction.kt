package com.elian.computeit.feature_tests.presentation.test_configuration

sealed interface TestConfigurationAction
{
    data class SelectOperationType(val symbol: String) : TestConfigurationAction
    data class EnterMinValue(val value: Int?) : TestConfigurationAction
    data class EnterMaxValue(val value: Int?) : TestConfigurationAction
    data class EnterTestTime(val value: Int?) : TestConfigurationAction
    object Start : TestConfigurationAction
}
package com.elian.computeit.feature_tests.presentation.test_configuration

sealed interface TestConfigurationAction
{
    data class SelectOperationType(val symbol: String) : TestConfigurationAction
    data class EnterRange(val min: Int?, val max: Int?) : TestConfigurationAction
    data class EnterSeconds(val seconds: Int?) : TestConfigurationAction
    object Start : TestConfigurationAction
}
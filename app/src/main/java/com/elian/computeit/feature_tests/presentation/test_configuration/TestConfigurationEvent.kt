package com.elian.computeit.feature_tests.presentation.test_configuration

sealed interface TestConfigurationEvent
{
    data class OnPlay(val args: List<Pair<String, Any>>) : TestConfigurationEvent
}
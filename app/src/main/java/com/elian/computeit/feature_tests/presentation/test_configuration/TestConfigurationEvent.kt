package com.elian.computeit.feature_tests.presentation.test_configuration

import com.elian.computeit.core.util.UiText

sealed interface TestConfigurationEvent
{
    data class OnStart(val args: List<Pair<String, Any>>) : TestConfigurationEvent
    data class OnShowErrorMessage(val error: UiText) : TestConfigurationEvent
}
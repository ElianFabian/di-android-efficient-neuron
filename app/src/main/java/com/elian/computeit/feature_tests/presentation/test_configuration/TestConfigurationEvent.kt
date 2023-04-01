package com.elian.computeit.feature_tests.presentation.test_configuration

import com.elian.computeit.core.util.UiText
import com.elian.computeit.feature_tests.domain.args.TestArgs

sealed interface TestConfigurationEvent
{
	data class OnStartTest(val args: TestArgs) : TestConfigurationEvent
	data class OnShowErrorMessage(val error: UiText) : TestConfigurationEvent
}
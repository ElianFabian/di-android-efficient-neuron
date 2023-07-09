package com.elian.computeit.feature_tests.presentation.test_configuration

import com.elian.computeit.core.util.UiText
import com.elian.computeit.feature_tests.domain.args.TestArgs

sealed interface TestConfigurationEvent {
	class OnStartTest(val args: TestArgs) : TestConfigurationEvent

	class OnShowMessage(
		val message: UiText,
		inline val action: (() -> Unit)? = null,
	) : TestConfigurationEvent
}
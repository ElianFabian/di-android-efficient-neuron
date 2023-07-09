package com.elian.computeit.core.domain.models

import com.elian.computeit.core.domain.errors.NumericFieldError
import com.elian.computeit.core.util.SimpleResource
import com.elian.computeit.core.util.UiText

data class TestConfigurationResult(
	val startOfRangeError: NumericFieldError? = null,
	val endOfRangeError: NumericFieldError? = null,
	val timeError: NumericFieldError? = null,
	val messageInfo: TestConfigurationResultMessage? = null,
	val resource: SimpleResource? = null,
)

sealed class TestConfigurationResultMessage(val content: UiText) {

	companion object {
		operator fun invoke(message: UiText) = RegularMessage(message)
	}

	class RegularMessage(message: UiText) : TestConfigurationResultMessage(message)
	class RangeValuesAreInverted(message: UiText) : TestConfigurationResultMessage(message)
}
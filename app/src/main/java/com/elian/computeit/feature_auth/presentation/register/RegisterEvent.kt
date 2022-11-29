package com.elian.computeit.feature_auth.presentation.register

import com.elian.computeit.core.util.UiText

sealed interface RegisterEvent
{
	object OnRegister : RegisterEvent
	data class OnShowErrorMessage(val error: UiText) : RegisterEvent
}
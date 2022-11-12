package com.elian.computeit.feature_auth.presentation.register

import com.elian.computeit.core.util.UiText

sealed interface RegisterEvent
{
    data class OnRegister(val args: List<Pair<String, Any>>) : RegisterEvent
    data class OnShowErrorMessage(val error: UiText) : RegisterEvent
}
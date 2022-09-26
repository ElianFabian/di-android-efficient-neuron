package com.elian.computeit.feature_auth.presentation.register

import com.elian.computeit.core.util.UiText

sealed interface RegisterEvent
{
    object Register : RegisterEvent
    data class ShowErrorMessage(val error: UiText) : RegisterEvent
}
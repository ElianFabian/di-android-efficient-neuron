package com.elian.computeit.feature_auth.presentation.login

import com.elian.computeit.core.util.UiText

sealed interface LoginEvent
{
    object Login : LoginEvent
    data class ShowErrorMessage(val error: UiText) : LoginEvent
}
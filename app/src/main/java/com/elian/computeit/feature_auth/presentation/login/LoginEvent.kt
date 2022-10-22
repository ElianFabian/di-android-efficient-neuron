package com.elian.computeit.feature_auth.presentation.login

import com.elian.computeit.core.util.UiText

sealed interface LoginEvent
{
    object OnLogin : LoginEvent
    data class OnShowErrorMessage(val error: UiText) : LoginEvent
}
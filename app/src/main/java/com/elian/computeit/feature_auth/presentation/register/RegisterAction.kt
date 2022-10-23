package com.elian.computeit.feature_auth.presentation.register

sealed interface RegisterAction
{
    data class EnterEmail(val value: String) : RegisterAction
    data class EnterPassword(val value: String) : RegisterAction
    data class EnterConfirmPassword(val value: String) : RegisterAction
    object Register : RegisterAction
}
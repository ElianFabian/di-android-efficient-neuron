package com.elian.computeit.feature_auth.presentation.register

sealed interface RegisterAction
{
    data class ReceivedEmail(val value: String) : RegisterAction
    data class ReceivedPassword(val value: String) : RegisterAction
    data class ReceivedConfirmPassword(val value: String) : RegisterAction
    object Register : RegisterAction
}
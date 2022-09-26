package com.elian.computeit.feature_auth.presentation.register

sealed interface RegisterAction
{
    data class EnteredEmail(val value: String) : RegisterAction
    data class EnteredPassword(val value: String) : RegisterAction
    data class EnteredConfirmPassword(val value: String) : RegisterAction
    object Register : RegisterAction
}
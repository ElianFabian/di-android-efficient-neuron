package com.elian.computeit.feature_auth.presentation.login

sealed interface LoginAction
{
    data class EnteredEmail(val value: String) : LoginAction
    data class EnteredPassword(val value: String) : LoginAction
    object Login : LoginAction
}
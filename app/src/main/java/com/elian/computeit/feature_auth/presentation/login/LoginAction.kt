package com.elian.computeit.feature_auth.presentation.login

sealed interface LoginAction {
	data class EnterUsername(val value: String) : LoginAction
	data class EnterPassword(val value: String) : LoginAction
	object Login : LoginAction
}
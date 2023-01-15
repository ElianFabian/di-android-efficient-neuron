package com.elian.computeit.feature_auth.presentation.login

import com.elian.computeit.core.util.Error

data class LoginState(
	val username: String = "",
	val usernameError: Error? = null,
	val password: String = "",
	val passwordError: Error? = null,
	val isLoading: Boolean = false,
)

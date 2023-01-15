package com.elian.computeit.feature_auth.presentation.register

import com.elian.computeit.core.util.Error

data class RegisterState(
	val username: String = "",
	val usernameError: Error? = null,
	val password: String = "",
	val passwordError: Error? = null,
	val confirmPassword: String = "",
	val confirmPasswordError: Error? = null,
	val isLoading: Boolean = false,
)
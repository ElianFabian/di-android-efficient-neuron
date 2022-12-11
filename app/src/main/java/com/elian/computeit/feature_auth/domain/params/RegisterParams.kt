package com.elian.computeit.feature_auth.domain.params

data class RegisterParams(
	val username: String,
	val password: String,
	val confirmPassword: String,
)

package com.elian.computeit.feature_auth.domain.use_case

import com.elian.computeit.core.domain.util.*
import com.elian.computeit.feature_auth.domain.model.RegisterResult
import com.elian.computeit.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class Register @Inject constructor(
	private val repository: AuthRepository,
)
{
	suspend operator fun invoke(
		username: String,
		password: String,
		confirmPassword: String,
	): RegisterResult
	{
		val nameError = validateName(username)
		val passwordError = validatePassword(password)
		val confirmPasswordError = validateConfirmPassword(confirmPassword, password)

		return if (checkIfError(nameError, passwordError, confirmPasswordError))
		{
			RegisterResult(
				usernameError = nameError,
				passwordError = passwordError,
				confirmPasswordError = confirmPasswordError,
			)
		}
		else RegisterResult(resource = repository.register(
			username = username,
			password = hash(password),
		))
	}
}
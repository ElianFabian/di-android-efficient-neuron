package com.elian.computeit.feature_auth.domain.use_case

import com.elian.computeit.core.domain.errors.TextFieldError
import com.elian.computeit.core.domain.util.checkIfError
import com.elian.computeit.core.domain.util.hash
import com.elian.computeit.feature_auth.domain.model.LoginResult
import com.elian.computeit.feature_auth.domain.params.LoginParams
import com.elian.computeit.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
	private val repository: AuthRepository,
) {
	suspend operator fun invoke(params: LoginParams): LoginResult {
		val usernameError = if (params.username.isBlank()) TextFieldError.Empty else null
		val passwordError = if (params.password.isBlank()) TextFieldError.Empty else null

		return if (checkIfError(usernameError, passwordError)) {
			LoginResult(
				usernameError = usernameError,
				passwordError = passwordError,
			)
		}
		else LoginResult(
			resource = repository.login(
				params.copy(
					password = hash(params.password),
				)
			)
		)
	}
}
package com.elian.computeit.feature_auth.domain.use_case

import com.elian.computeit.core.domain.util.checkIfError
import com.elian.computeit.core.domain.util.hash
import com.elian.computeit.core.domain.util.validateConfirmPassword
import com.elian.computeit.core.domain.util.validateName
import com.elian.computeit.core.domain.util.validatePassword
import com.elian.computeit.feature_auth.domain.model.RegisterResult
import com.elian.computeit.feature_auth.domain.params.RegisterParams
import com.elian.computeit.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
	private val repository: AuthRepository,
) {
	suspend operator fun invoke(params: RegisterParams): RegisterResult {
		val usernameError = validateName(params.username)
		val passwordError = validatePassword(params.password)
		val confirmPasswordError = validateConfirmPassword(params.confirmPassword, params.password)

		return if (checkIfError(usernameError, passwordError, confirmPasswordError)) {
			RegisterResult(
				usernameError = usernameError,
				passwordError = passwordError,
				confirmPasswordError = confirmPasswordError,
			)
		}
		else RegisterResult(
			resource = repository.register(
				params.copy(
					password = hash(params.password)
				)
			)
		)
	}
}
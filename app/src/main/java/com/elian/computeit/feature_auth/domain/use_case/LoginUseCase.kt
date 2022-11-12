package com.elian.computeit.feature_auth.domain.use_case

import com.elian.computeit.core.domain.util.HashingUtil
import com.elian.computeit.feature_auth.domain.models.LoginResult
import com.elian.computeit.feature_auth.domain.repository.AuthRepository
import com.elian.computeit.feature_auth.presentation.util.AuthError
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository,
)
{
    suspend operator fun invoke(email: String, password: String): LoginResult
    {
        val emailError = if (email.isBlank()) AuthError.Empty else null
        val passwordError = if (password.isBlank()) AuthError.Empty else null

        if (emailError != null || passwordError != null)
        {
            return LoginResult(emailError, passwordError)
        }

        val hashedPassword = HashingUtil.hash(password)

        return LoginResult(result = repository.login(email, hashedPassword))
    }
}
package com.elian.computeit.feature_auth.domain.use_case

import com.elian.computeit.core.domain.util.checkErrors
import com.elian.computeit.core.domain.util.hash
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

        if (checkErrors(emailError, passwordError))
        {
            return LoginResult(
                emailError = emailError,
                passwordError = passwordError,
            )
        }

        val hashedPassword = hash(password)

        return LoginResult(result = repository.login(email, hashedPassword))
    }
}
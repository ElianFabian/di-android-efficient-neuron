package com.elian.computeit.feature_auth.domain.use_case

import com.elian.computeit.core.domain.util.checkIfError
import com.elian.computeit.core.domain.util.hash
import com.elian.computeit.feature_auth.domain.models.LoginResult
import com.elian.computeit.feature_auth.domain.repository.AuthRepository
import com.elian.computeit.feature_auth.presentation.util.AuthError
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository,
)
{
    suspend operator fun invoke(username: String, password: String): LoginResult
    {
        val usernameError = if (username.isBlank()) AuthError.Empty else null
        val passwordError = if (password.isBlank()) AuthError.Empty else null

        return if (checkIfError(usernameError, passwordError))
        {
            LoginResult(
                usernameError = usernameError,
                passwordError = passwordError,
            )
        }
        else LoginResult(resource = repository.login(
            username = username,
            password = hash(password),
        ))
    }
}
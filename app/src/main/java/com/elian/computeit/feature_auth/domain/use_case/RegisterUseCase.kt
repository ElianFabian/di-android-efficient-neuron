package com.elian.computeit.feature_auth.domain.use_case

import com.elian.computeit.core.domain.util.hash
import com.elian.computeit.core.domain.util.validateConfirmPassword
import com.elian.computeit.core.domain.util.validateEmail
import com.elian.computeit.core.domain.util.validatePassword
import com.elian.computeit.feature_auth.domain.models.RegisterResult
import com.elian.computeit.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository,
)
{
    suspend operator fun invoke(email: String, password: String, confirmPassword: String): RegisterResult
    {
        val emailError = validateEmail(email)
        val passwordError = validatePassword(password)
        val confirmPasswordError = validateConfirmPassword(confirmPassword, password)

        if (emailError != null || passwordError != null || confirmPasswordError != null)
        {
            return RegisterResult(
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError,
            )
        }

        val hashedPassword = hash(password)

        return RegisterResult(result = repository.register(email, hashedPassword))
    }
}
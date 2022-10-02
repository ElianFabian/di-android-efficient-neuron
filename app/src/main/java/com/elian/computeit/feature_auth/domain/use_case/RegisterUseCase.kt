package com.elian.computeit.feature_auth.domain.use_case

import com.elian.computeit.core.domain.util.HashingUtil
import com.elian.computeit.feature_auth.domain.models.RegisterResult
import com.elian.computeit.feature_auth.domain.repository.AuthRepository
import com.elian.computeit.core.domain.util.ValidationUtil
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository,
)
{
    suspend operator fun invoke(email: String, password: String, confirmPassword: String): RegisterResult
    {
        val emailError = ValidationUtil.validateEmail(email)
        val passwordError = ValidationUtil.validatePassword(password)
        val confirmPasswordError = ValidationUtil.validateConfirmPassword(confirmPassword, password)

        if (emailError != null || passwordError != null || confirmPasswordError != null)
        {
            return RegisterResult(
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError
            )
        }
        
        val hashedPassword = HashingUtil.hash(password)

        return RegisterResult(result = repository.register(email, hashedPassword))
    }
}
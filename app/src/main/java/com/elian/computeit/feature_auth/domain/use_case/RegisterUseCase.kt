package com.elian.computeit.feature_auth.domain.use_case

import com.elian.computeit.core.domain.util.*
import com.elian.computeit.feature_auth.domain.models.RegisterResult
import com.elian.computeit.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository,
)
{
    suspend operator fun invoke(
        email: String,
        name: String,
        password: String,
        confirmPassword: String,
    ): RegisterResult
    {
        val emailError = validateEmail(email)
        val nameError = validateName(name)
        val passwordError = validatePassword(password)
        val confirmPasswordError = validateConfirmPassword(confirmPassword, password)

        if (checkIfError(emailError, nameError, passwordError, confirmPasswordError))
        {
            return RegisterResult(
                emailError = emailError,
                nameError = nameError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError,
            )
        }
        return RegisterResult(result = repository.register(
            email = email,
            name = name,
            password = hash(password),
        ))
    }
}
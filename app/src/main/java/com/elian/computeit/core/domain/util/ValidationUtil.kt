package com.elian.computeit.core.domain.util

import android.util.Patterns
import com.elian.computeit.feature_auth.presentation.util.AuthError

object ValidationUtil
{
    private const val MIN_PASSWORD_LENGTH = 8
    private const val MAX_PASSWORD_LENGTH = 20
    private const val SPECIAL_CHARACTERS = "!?/\\$%&#."
    private const val SET_OF_DIGITS = "0123456789"


    fun validateEmail(email: String): AuthError?
    {
        val trimmedEmail = email.trim()

        return when
        {
            trimmedEmail.isBlank()                                       -> AuthError.ValueEmpty
            Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches().not() -> AuthError.ValueInvalid(example = "abcd@gmail.com")

            else                                                         -> null
        }
    }

    fun validatePassword(password: String): AuthError?
    {
        val trimmedPassword = password.trim()

        return when
        {
            trimmedPassword.isBlank()                              -> AuthError.ValueEmpty
            trimmedPassword.length < MIN_PASSWORD_LENGTH           -> AuthError.ValueTooShort(minLength = MIN_PASSWORD_LENGTH)
            trimmedPassword.any { it.isDigit() }.not()             -> AuthError.ValueInvalid(validCharacters = SET_OF_DIGITS, minCharacterCount = 1)
            trimmedPassword.any { it in SPECIAL_CHARACTERS }.not() -> AuthError.ValueInvalid(validCharacters = SPECIAL_CHARACTERS, minCharacterCount = 1)
            trimmedPassword.length > MAX_PASSWORD_LENGTH           -> AuthError.ValueTooLong(maxLength = MAX_PASSWORD_LENGTH)

            else                                                   -> null
        }
    }

    fun validateConfirmPassword(confirmPassword: String, password: String): AuthError?
    {
        val trimmedConfirmPassword = confirmPassword.trim()
        val trimmedPassword = password.trim()

        return when
        {
            trimmedConfirmPassword.isBlank()          -> AuthError.ValueEmpty
            trimmedConfirmPassword != trimmedPassword -> AuthError.ValueInvalid()

            else                                      -> null
        }
    }
}
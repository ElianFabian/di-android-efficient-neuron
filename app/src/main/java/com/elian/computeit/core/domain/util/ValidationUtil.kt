package com.elian.computeit.core.domain.util

import android.util.Patterns
import com.elian.computeit.feature_auth.presentation.util.AuthError

private const val SET_OF_DIGITS = "0123456789"

private const val USERNAME_MIN_LENGTH = 2
private const val USERNAME_MAX_LENGTH = 30
private const val USERNAME_VALID_CHARACTERS = "!?$&#._-"
private const val USERNAME_PATTERN = """[\w$USERNAME_VALID_CHARACTERS]+"""
private val USERNAME_REGEX = USERNAME_PATTERN.toRegex()

private const val PASSWORD_MIN_LENGTH = 8
private const val PASSWORD_MAX_LENGTH = 20
private const val PASSWORD_SPECIAL_CHARACTERS = "!?/\\$%&#."


fun validateEmail(email: String): AuthError?
{
    val trimmedEmail = email.trim()

    return when
    {
        trimmedEmail.isBlank()                                  -> AuthError.Empty
        !trimmedEmail.matches(Patterns.EMAIL_ADDRESS.toRegex()) -> AuthError.Invalid(example = "abcd@gmail.com")

        else                                                    -> null
    }
}

fun validateUsername(username: String): AuthError?
{
    val trimmedUsername = username.trim()

    return when
    {
        trimmedUsername.isBlank()                    -> AuthError.Empty
        !trimmedUsername.matches(USERNAME_REGEX)     -> AuthError.Invalid(validCharacters = USERNAME_VALID_CHARACTERS)
        trimmedUsername.length < USERNAME_MIN_LENGTH -> AuthError.TooShort(minLength = USERNAME_MIN_LENGTH)
        trimmedUsername.length > USERNAME_MAX_LENGTH -> AuthError.TooLong(maxLength = USERNAME_MAX_LENGTH)

        else                                         -> null
    }
}

fun validatePassword(password: String): AuthError?
{
    val trimmedPassword = password.trim()

    return when
    {
        trimmedPassword.isBlank()                                  -> AuthError.Empty
        trimmedPassword.length < PASSWORD_MIN_LENGTH               -> AuthError.TooShort(minLength = PASSWORD_MIN_LENGTH)
        !trimmedPassword.any { it.isDigit() }                      -> AuthError.Invalid(validCharacters = SET_OF_DIGITS, minCharacterCount = 1)
        !trimmedPassword.any { it in PASSWORD_SPECIAL_CHARACTERS } -> AuthError.Invalid(validCharacters = PASSWORD_SPECIAL_CHARACTERS, minCharacterCount = 1)
        trimmedPassword.length > PASSWORD_MAX_LENGTH               -> AuthError.TooLong(maxLength = PASSWORD_MAX_LENGTH)

        else                                                       -> null
    }
}

fun validateConfirmPassword(confirmPassword: String, password: String): AuthError?
{
    val trimmedConfirmPassword = confirmPassword.trim()
    val trimmedPassword = password.trim()

    return when
    {
        trimmedConfirmPassword.isBlank()          -> AuthError.Empty
        trimmedConfirmPassword != trimmedPassword -> AuthError.Invalid()

        else                                      -> null
    }
}
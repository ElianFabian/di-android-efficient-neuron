package com.elian.computeit.core.domain.util

import com.elian.computeit.feature_auth.presentation.util.AuthError

private const val SET_OF_DIGITS = "0123456789"

private const val NAME_MIN_LENGTH = 2
private const val NAME_MAX_LENGTH = 30
private const val NAME_VALID_CHARACTERS = "!?$&#._-"
private const val NAME_PATTERN = """[\w$NAME_VALID_CHARACTERS]+"""
private val NAME_REGEX = NAME_PATTERN.toRegex()

private const val PASSWORD_MIN_LENGTH = 8
private const val PASSWORD_MAX_LENGTH = 20
private const val PASSWORD_SPECIAL_CHARACTERS = """!?/\$€%&#.=+-"""


fun validateName(name: String): AuthError?
{
    val trimmedName = name.trim()

    return when
    {
        trimmedName.isBlank()                -> AuthError.Empty
        !trimmedName.matches(NAME_REGEX)     -> AuthError.Invalid(validCharacters = NAME_VALID_CHARACTERS)
        trimmedName.length < NAME_MIN_LENGTH -> AuthError.TooShort(minLength = NAME_MIN_LENGTH)
        trimmedName.length > NAME_MAX_LENGTH -> AuthError.TooLong(maxLength = NAME_MAX_LENGTH)

        else                                 -> null
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
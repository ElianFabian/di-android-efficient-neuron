package com.elian.computeit.core.domain.util

import com.elian.computeit.core.domain.errors.TextFieldError
import com.elian.computeit.core.util.Error

private const val SET_OF_DIGITS = "0123456789"

private const val NAME_MIN_LENGTH = 2
private const val NAME_MAX_LENGTH = 30
private const val NAME_VALID_CHARACTERS = "!?$&#._-"
private val NAME_REGEX = """[a-z$NAME_VALID_CHARACTERS]+""".toRegex()

private const val PASSWORD_MIN_LENGTH = 8
private const val PASSWORD_MAX_LENGTH = 20
private const val PASSWORD_SPECIAL_CHARACTERS = """@!?/\$€%&#.=+-"""


fun validateName(name: String): Error? {
	val trimmedName = name.trim()

	return when {
		trimmedName.isBlank()                -> TextFieldError.Empty
		!trimmedName.matches(NAME_REGEX)     -> TextFieldError.Invalid(validCharacters = NAME_VALID_CHARACTERS)
		trimmedName.length < NAME_MIN_LENGTH -> TextFieldError.TooShort(minLength = NAME_MIN_LENGTH)
		trimmedName.length > NAME_MAX_LENGTH -> TextFieldError.TooLong(maxLength = NAME_MAX_LENGTH)

		else                                 -> null
	}
}

fun validatePassword(password: String): Error? {
	val trimmedPassword = password.trim()

	return when {
		trimmedPassword.isBlank()                                  -> TextFieldError.Empty
		trimmedPassword.length < PASSWORD_MIN_LENGTH               -> TextFieldError.TooShort(minLength = PASSWORD_MIN_LENGTH)
		!trimmedPassword.any { it.isDigit() }                      -> TextFieldError.Invalid(validCharacters = SET_OF_DIGITS, minCharacterCount = 1)
		!trimmedPassword.any { it in PASSWORD_SPECIAL_CHARACTERS } -> TextFieldError.Invalid(validCharacters = PASSWORD_SPECIAL_CHARACTERS, minCharacterCount = 1)
		trimmedPassword.length > PASSWORD_MAX_LENGTH               -> TextFieldError.TooLong(maxLength = PASSWORD_MAX_LENGTH)

		else                                                       -> null
	}
}

fun validateConfirmPassword(confirmPassword: String, password: String): Error? {
	val trimmedConfirmPassword = confirmPassword.trim()
	val trimmedPassword = password.trim()

	return when {
		trimmedConfirmPassword.isBlank()          -> TextFieldError.Empty
		trimmedConfirmPassword != trimmedPassword -> TextFieldError.Invalid()

		else                                      -> null
	}
}
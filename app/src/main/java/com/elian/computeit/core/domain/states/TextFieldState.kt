package com.elian.computeit.core.domain.states

import com.elian.computeit.core.util.Error

data class TextFieldState(
	val text: String = "",
	val error: Error? = null,
)

sealed interface TextFieldError : Error
{
	object Empty : TextFieldError

	data class Invalid(
		val validCharacters: String? = null,
		val minCharacterCount: Int? = null,
		val example: String? = null,
	) : TextFieldError

	data class TooShort(val minLength: Int? = null) : TextFieldError
	data class TooLong(val maxLength: Int? = null) : TextFieldError
}
package com.elian.computeit.core.domain.errors

import com.elian.computeit.core.util.Error

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
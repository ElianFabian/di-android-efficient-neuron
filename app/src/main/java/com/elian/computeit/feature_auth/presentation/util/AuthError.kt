package com.elian.computeit.feature_auth.presentation.util

import com.elian.computeit.core.util.Error

sealed class AuthError : Error()
{
    object Empty : AuthError()

    data class Invalid(
        val validCharacters: String? = null,
        val minCharacterCount: Int? = null,
        val example: String? = null,
    ) : AuthError()

    data class TooShort(val minLength: Int? = null) : AuthError()
    data class TooLong(val maxLength: Int? = null) : AuthError()
}
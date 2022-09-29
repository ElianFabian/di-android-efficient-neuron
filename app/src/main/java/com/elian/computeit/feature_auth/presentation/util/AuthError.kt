package com.elian.computeit.feature_auth.presentation.util

import com.elian.computeit.core.util.Error

// From: https://github.com/philipplackner/SocialNetworkTwitch/blob/development/app/src/main/java/com/plcoding/socialnetworktwitch/feature_auth/presentation/util/AuthError.kt
sealed class AuthError : Error()
{
    object ValueEmpty : AuthError()

    data class ValueInvalid(
        val validCharacters: String? = null,
        val minCharacterCount: Int? = null,
        val example: String? = null,
    ) : AuthError()

    data class ValueTooShort(val minLength: Int? = null) : AuthError()
    data class ValueTooLong(val maxLength: Int? = null) : AuthError()
}
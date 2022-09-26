package com.elian.computeit.feature_auth.presentation.util

import com.elian.computeit.core.util.Error

// From: https://github.com/philipplackner/SocialNetworkTwitch/blob/development/app/src/main/java/com/plcoding/socialnetworktwitch/feature_auth/presentation/util/AuthError.kt
sealed class AuthError : Error()
{
    object ValueEmpty : AuthError()
    object ValueInvalid : AuthError()
    object ValueTooShort : AuthError()
    object ValueTooLong : AuthError()
}
package com.elian.computeit.feature_auth.domain.models

import com.elian.computeit.core.util.SimpleResource
import com.elian.computeit.feature_auth.presentation.util.AuthError

// From: https://github.com/philipplackner/SocialNetworkTwitch/blob/development/app/src/main/java/com/plcoding/socialnetworktwitch/feature_auth/domain/models/LoginResult.kt
data class LoginResult(
    val emailError: AuthError? = null,
    val passwordError: AuthError? = null,
    val result: SimpleResource? = null,
)

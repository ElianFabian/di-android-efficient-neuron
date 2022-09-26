package com.elian.computeit.feature_auth.domain.models

import com.elian.computeit.core.util.SimpleResource
import com.elian.computeit.feature_auth.presentation.util.AuthError

data class RegisterResult(
    val emailError: AuthError? = null,
    val passwordError: AuthError? = null,
    val confirmPasswordError: AuthError? = null,
    val result: SimpleResource? = null
)
package com.elian.computeit.feature_auth.domain.model

import com.elian.computeit.core.util.Error
import com.elian.computeit.core.util.SimpleResource

data class LoginResult(
	val usernameError: Error? = null,
	val passwordError: Error? = null,
	val resource: SimpleResource? = null,
)

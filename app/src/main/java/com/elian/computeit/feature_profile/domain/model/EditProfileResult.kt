package com.elian.computeit.feature_profile.domain.model

import com.elian.computeit.core.util.Error
import com.elian.computeit.core.util.SimpleResource

data class EditProfileResult(
	val usernameError: Error? = null,
	val resource: SimpleResource? = null,
)
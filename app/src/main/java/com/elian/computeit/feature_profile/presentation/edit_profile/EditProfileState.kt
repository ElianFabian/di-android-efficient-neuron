package com.elian.computeit.feature_profile.presentation.edit_profile

import com.elian.computeit.core.util.Error

data class EditProfileState(
	val username: String = "",
	val usernameError: Error? =  null,
	val biography: String = "",
	val profilePicBytes: List<Byte> = emptyList(),
	val isLoading: Boolean = false,
)

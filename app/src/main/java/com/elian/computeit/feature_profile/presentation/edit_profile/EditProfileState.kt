package com.elian.computeit.feature_profile.presentation.edit_profile

import com.elian.computeit.core.domain.states.TextFieldState

data class EditProfileState(
	val isLoading: Boolean = false,
	val usernameField: TextFieldState = TextFieldState(),
	val biography: String = "",
	val profilePicBytes: List<Byte> = emptyList(),
)

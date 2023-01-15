package com.elian.computeit.feature_profile.presentation

data class ProfileState(
	val username: String = "",
	val biography: String = "",
	val profilePicBytes: List<Byte> = emptyList(),
)

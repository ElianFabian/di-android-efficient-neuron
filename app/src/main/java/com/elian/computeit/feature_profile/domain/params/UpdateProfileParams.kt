package com.elian.computeit.feature_profile.domain.params

data class UpdateProfileParams(
	val userUuid: String,
	val username: String,
	val biography: String,
	val profilePicBytes: List<Byte>,
)

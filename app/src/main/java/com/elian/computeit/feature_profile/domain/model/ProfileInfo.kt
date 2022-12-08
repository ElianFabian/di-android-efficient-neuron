package com.elian.computeit.feature_profile.domain.model

data class ProfileInfo(
	val username: String,
	val biography: String,
	val profilePicBytes: List<Byte>,
	val createdAt: String,
)

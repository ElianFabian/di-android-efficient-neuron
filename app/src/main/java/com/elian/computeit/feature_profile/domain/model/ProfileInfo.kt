package com.elian.computeit.feature_profile.domain.model

data class ProfileInfo(
	val username: String,
	val biography: String,
	val profilePicUrl: String?,
	val createdAt: String,
)

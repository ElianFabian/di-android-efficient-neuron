package com.elian.computeit.feature_profile.domain.repository

import com.elian.computeit.core.util.SimpleResource
import com.elian.computeit.feature_profile.domain.model.ProfileInfo

interface ProfileRepository
{
	suspend fun getProfileInfo(): ProfileInfo

	suspend fun updateProfileInfo(
		username: String,
		biography: String,
		profilePicBytes: List<Byte>,
	): SimpleResource

	suspend fun logout()
}
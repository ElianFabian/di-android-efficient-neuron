package com.elian.computeit.feature_profile.domain.repository

import com.elian.computeit.core.util.SimpleResource
import com.elian.computeit.feature_profile.domain.model.ProfileInfo
import com.elian.computeit.feature_profile.domain.params.UpdateProfileParams

interface ProfileRepository {
	suspend fun getProfileInfo(userUuid: String): ProfileInfo

	suspend fun updateProfileInfo(params: UpdateProfileParams): SimpleResource

	suspend fun logout()
}
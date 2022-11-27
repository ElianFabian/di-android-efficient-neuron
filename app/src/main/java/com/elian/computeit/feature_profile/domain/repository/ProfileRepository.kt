package com.elian.computeit.feature_profile.domain.repository

import com.elian.computeit.core.util.SimpleResource
import com.elian.computeit.feature_profile.domain.model.ProfileInfo
import kotlinx.coroutines.flow.Flow

interface ProfileRepository
{
    fun getProfileInfo(): Flow<ProfileInfo>
    suspend fun updateProfileInfo(
        username: String, 
        biography: String, 
        //profilePicUrl: Uri
    ): SimpleResource
}
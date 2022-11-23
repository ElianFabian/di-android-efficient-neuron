package com.elian.computeit.feature_profile.domain.repository

import com.elian.computeit.feature_profile.domain.model.ProfileInfo
import kotlinx.coroutines.flow.Flow

interface ProfileRepository
{
    suspend fun getProfileInfo(): Flow<ProfileInfo>
}
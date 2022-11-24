package com.elian.computeit.feature_profile.domain.repository

import com.elian.computeit.feature_profile.domain.model.PrivateProfileInfo
import kotlinx.coroutines.flow.Flow

interface ProfileRepository
{
    fun getProfileInfo(): Flow<PrivateProfileInfo>
}
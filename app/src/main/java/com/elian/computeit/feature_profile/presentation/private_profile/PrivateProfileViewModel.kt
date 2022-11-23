package com.elian.computeit.feature_profile.presentation.private_profile

import androidx.lifecycle.ViewModel
import com.elian.computeit.feature_profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrivateProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
) : ViewModel()
{
    suspend fun getProfileInfo() = repository.getProfileInfo()
}
package com.elian.computeit.feature_profile.presentation.private_profile

import androidx.lifecycle.ViewModel
import com.elian.computeit.feature_profile.domain.use_case.GetProfileInfo
import com.elian.computeit.feature_profile.domain.use_case.Logout
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrivateProfileViewModel @Inject constructor(
	private val getProfileInfoUseCase: GetProfileInfo,
	private val logoutUseCase: Logout,
) : ViewModel()
{
	suspend fun getProfileInfo() = getProfileInfoUseCase()
	suspend fun logout() = logoutUseCase()
}
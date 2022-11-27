package com.elian.computeit.feature_profile.presentation.private_profile

import androidx.lifecycle.ViewModel
import com.elian.computeit.feature_profile.domain.use_case.GetProfileInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrivateProfileViewModel @Inject constructor(
    val getProfileInfo: GetProfileInfoUseCase,
) : ViewModel()
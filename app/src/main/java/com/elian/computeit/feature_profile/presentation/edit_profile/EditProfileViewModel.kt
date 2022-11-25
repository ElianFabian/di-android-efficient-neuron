package com.elian.computeit.feature_profile.presentation.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.feature_profile.domain.model.ProfileInfo
import com.elian.computeit.feature_profile.domain.use_case.GetProfileInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getProfileInfo: GetProfileInfoUseCase,
) : ViewModel()
{
    init
    {
        viewModelScope.launch()
        {
            getProfileInfo().collect { _infoState.value = it }
        }
    }


    private val _infoState = MutableStateFlow<ProfileInfo?>(null)
    val infoState = _infoState.asStateFlow()
}
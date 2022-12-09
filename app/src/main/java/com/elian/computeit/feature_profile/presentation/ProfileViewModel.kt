package com.elian.computeit.feature_profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.domain.states.TextFieldState
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.feature_profile.domain.use_case.GetProfileInfo
import com.elian.computeit.feature_profile.domain.use_case.Logout
import com.elian.computeit.feature_profile.domain.use_case.ValidateProfile
import com.elian.computeit.feature_profile.presentation.ProfileAction.*
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileEvent
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileEvent.OnSave
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileEvent.OnShowErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
	private val getProfileInfoUseCase: GetProfileInfo,
	private val logoutUseCase: Logout,
	private val validateProfile: ValidateProfile,
) : ViewModel()
{
	init
	{
		viewModelScope.launch()
		{
			getProfileInfoUseCase().apply()
			{
				_usernameState.update { it.copy(text = username) }
				_biographyState.value = biography
				_profilePicState.value = profilePicBytes
				_createdAtState.value = createdAt
			}

			_privateProfileViewsAreGoneState.value = false
		}
	}


	private val _profilePicState = MutableStateFlow(emptyList<Byte>())
	val profilePicState = _profilePicState.asStateFlow()

	private val _usernameState = MutableStateFlow(TextFieldState())
	val usernameState = _usernameState.asStateFlow()

	private val _biographyState = MutableStateFlow("")
	val biographyState = _biographyState.asStateFlow()

	private val _createdAtState = MutableStateFlow("")
	val createdAtState = _createdAtState.asStateFlow()


	private val _editProfileEventFlow = Channel<EditProfileEvent>()
	val editProfileEventFlow = _editProfileEventFlow.receiveAsFlow()

	private val _editProfileIsLoadingState = MutableStateFlow(false)
	val editProfileIsLoadingState = _editProfileIsLoadingState.asStateFlow()


	private val _privateProfileViewsAreGoneState = MutableStateFlow(true)
	val privateProfileViewsAreGoneState = _privateProfileViewsAreGoneState.asStateFlow()


	fun onAction(action: ProfileAction)
	{
		when (action)
		{
			is EnterUsername   -> _usernameState.update { it.copy(text = action.value, error = null) }
			is EnterBiography  -> _biographyState.value = action.value
			is EnterProfilePic ->
			{
				if (action.value.isNotEmpty()) _profilePicState.value = action.value
			}
			is Save            -> viewModelScope.launch()
			{
				_editProfileIsLoadingState.value = true

				validateProfile(
					username = _usernameState.value.text,
					biography = _biographyState.value,
					profilePicBytes = _profilePicState.value,
				).also { result ->

					_usernameState.update { it.copy(error = result.usernameError) }

					when (val resource = result.resource)
					{
						is Resource.Error   -> _editProfileEventFlow.send(OnShowErrorMessage(resource.uiText ?: UiText.unknownError()))
						is Resource.Success -> _editProfileEventFlow.send(OnSave)
						else                -> Unit
					}

					_editProfileIsLoadingState.value = false
				}
			}
		}
	}

	suspend fun logout() = logoutUseCase()
}
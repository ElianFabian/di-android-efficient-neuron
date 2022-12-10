package com.elian.computeit.feature_profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.feature_profile.domain.use_case.ProfileUseCases
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileAction
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileAction.*
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileEvent
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileEvent.OnSave
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileEvent.OnShowErrorMessage
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
	private val useCases: ProfileUseCases,
) : ViewModel()
{
	private val _profilePicState = MutableStateFlow(emptyList<Byte>())
	val profilePicState = _profilePicState.asStateFlow()

	private val _usernameState = MutableStateFlow("")
	val usernameState = _usernameState.asStateFlow()

	private val _biographyState = MutableStateFlow("")
	val biographyState = _biographyState.asStateFlow()

	private val _createdAtState = MutableStateFlow("")
	val createdAtState = _createdAtState.asStateFlow()


	private val _editProfileEventFlow = Channel<EditProfileEvent>()
	val editProfileEventFlow = _editProfileEventFlow.receiveAsFlow()

	private val _editProfileState = MutableStateFlow(EditProfileState())
	val editProfileState = _editProfileState.asStateFlow()


	private val _privateProfileViewsAreGoneState = MutableStateFlow(true)
	val privateProfileViewsAreGoneState = _privateProfileViewsAreGoneState.asStateFlow()


	init
	{
		initialize()
	}


	fun onAction(action: EditProfileAction)
	{
		when (action)
		{
			is EnterProfilePic -> _editProfileState.update()
			{
				it.copy(profilePicBytes = action.value)
			}
			is EnterUsername   -> _editProfileState.update()
			{
				it.copy(usernameField = it.usernameField.copy(text = action.value, error = null))
			}
			is EnterBiography  -> _editProfileState.update()
			{
				it.copy(biography = action.value)
			}
			is Save            -> viewModelScope.launch()
			{
				_editProfileState.update { it.copy(isLoading = true) }

				useCases.updateProfile(
					username = _editProfileState.value.usernameField.text,
					biography = _editProfileState.value.biography,
					profilePicBytes = _editProfileState.value.profilePicBytes,
				).also { result ->

					_editProfileState.update()
					{
						it.copy(usernameField = it.usernameField.copy(error = result.usernameError))
					}

					when (val resource = result.resource)
					{
						is Resource.Error   -> _editProfileEventFlow.send(OnShowErrorMessage(resource.uiText ?: UiText.unknownError()))
						is Resource.Success ->
						{
							_editProfileState.value.apply()
							{
								_profilePicState.value = profilePicBytes
								_usernameState.value = usernameField.text
								_biographyState.value = biography
							}

							_editProfileEventFlow.send(OnSave)
						}
						else                -> Unit
					}

					_editProfileState.update { it.copy(isLoading = false) }
				}
			}
		}
	}

	suspend fun logout() = useCases.logout()


	private fun initialize()
	{
		viewModelScope.launch()
		{
			combine(_profilePicState, _usernameState, _biographyState) { profileBytes, username, biography ->

				_editProfileState.value.copy(
					profilePicBytes = profileBytes,
					usernameField = _editProfileState.value.usernameField.copy(text = username),
					biography = biography,
				)
			}.collect()
			{
				_editProfileState.value = it
			}
		}
		viewModelScope.launch()
		{
			useCases.getProfileInfo().apply()
			{
				_usernameState.value = username
				_biographyState.value = biography
				_profilePicState.value = profilePicBytes
				_createdAtState.value = createdAt
			}

			_privateProfileViewsAreGoneState.value = false
		}
	}
}
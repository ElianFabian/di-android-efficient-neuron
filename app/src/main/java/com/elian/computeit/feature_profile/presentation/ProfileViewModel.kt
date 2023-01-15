package com.elian.computeit.feature_profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.feature_profile.domain.params.UpdateProfileParams
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
	private val _profileState = MutableStateFlow<ProfileState?>(null)
	val profileState = _profileState.asStateFlow()

	private val _editProfileState = MutableStateFlow(EditProfileState())
	val editProfileState = _editProfileState.asStateFlow()

	private val _editProfileEventFlow = Channel<EditProfileEvent>()
	val editProfileEventFlow = _editProfileEventFlow.receiveAsFlow()

	private val _privateProfileIsLoadingState = MutableStateFlow(false)
	val privateProfileIsLoadingState = _privateProfileIsLoadingState.asStateFlow()


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

				useCases.updateProfile(UpdateProfileParams(
					userUuid = useCases.getOwnUserUuid(),
					username = _editProfileState.value.usernameField.text,
					biography = _editProfileState.value.biography,
					profilePicBytes = _editProfileState.value.profilePicBytes,
				)).also { result ->

					_editProfileState.update()
					{
						it.copy(usernameField = it.usernameField.copy(error = result.usernameError))
					}

					when (val resource = result.resource)
					{
						is Resource.Error   -> _editProfileEventFlow.send(OnShowErrorMessage(resource.uiText ?: UiText.unknownError()))
						is Resource.Success ->
						{
							val stateValue = _editProfileState.value
							_profileState.update()
							{
								it?.copy(
									profilePicBytes = stateValue.profilePicBytes,
									username = stateValue.usernameField.text,
									biography = stateValue.biography,
								)
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
			val stateValue = _editProfileState.value
			_profileState.filterNotNull().map()
			{
				stateValue.copy(
					profilePicBytes = it.profilePicBytes,
					usernameField = stateValue.usernameField.copy(text = it.username),
					biography = it.biography,
				)
			}.collect()
			{
				_editProfileState.value = it
			}
		}
		viewModelScope.launch()
		{
			_privateProfileIsLoadingState.value = true

			val info = useCases.getProfileInfo(useCases.getOwnUserUuid())

			_profileState.value = ProfileState(
				username = info.username,
				biography = info.biography,
				profilePicBytes = info.profilePicBytes,
				createdAt = info.createdAt,
			)

			_privateProfileIsLoadingState.value = false
		}
	}
}
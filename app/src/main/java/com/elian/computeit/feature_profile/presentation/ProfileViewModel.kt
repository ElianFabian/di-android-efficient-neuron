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
import com.elian.computeit.feature_profile.presentation.private_profile.PrivateProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
	private val useCases: ProfileUseCases,
) : ViewModel() {

	private val _sharedState = MutableStateFlow<ProfileState?>(null)
	val sharedState = _sharedState.asStateFlow()

	private val _privateProfileState = MutableStateFlow<PrivateProfileState?>(null)
	val privateProfileState = _privateProfileState.asStateFlow()

	private val _editProfileState = MutableStateFlow(EditProfileState())
	val editProfileState = _editProfileState.asStateFlow()

	private val _editProfileEventFlow = Channel<EditProfileEvent>()
	val editProfileEventFlow = _editProfileEventFlow.receiveAsFlow()


	init {
		initialize()
	}


	fun onAction(action: EditProfileAction) {
		when (action) {
			is EnterProfilePic -> _editProfileState.update { it.copy(profilePicBytes = action.value) }
			is EnterUsername   -> _editProfileState.update { it.copy(username = action.value, usernameError = null) }
			is EnterBiography  -> _editProfileState.update { it.copy(biography = action.value) }
			is Save            -> viewModelScope.launch {
				_editProfileState.update { it.copy(isLoading = true) }

				useCases.updateProfile(
					UpdateProfileParams(
						userUuid = useCases.getOwnUserUuid(),
						username = _editProfileState.value.username,
						biography = _editProfileState.value.biography,
						profilePicBytes = _editProfileState.value.profilePicBytes,
					)
				).also { result ->

					_editProfileState.value = _editProfileState.value.copy(
						usernameError = result.usernameError
					)

					when (val resource = result.resource) {
						is Resource.Error   -> _editProfileEventFlow.send(OnShowErrorMessage(resource.uiText ?: UiText.unknownError()))
						is Resource.Success -> {

							_sharedState.value = _sharedState.value?.copy(
								profilePicBytes = _editProfileState.value.profilePicBytes,
								username = _editProfileState.value.username,
								biography = _editProfileState.value.biography,
							)

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


	private fun initialize() {
		viewModelScope.launch {
			_sharedState.filterNotNull().map { state ->
				_editProfileState.value.copy(
					profilePicBytes = state.profilePicBytes,
					username = state.username,
					biography = state.biography,
				)
			}.collect {
				_editProfileState.value = it
			}
		}
		viewModelScope.launch {
			_privateProfileState.value = PrivateProfileState(isLoading = true)

			val info = useCases.getProfileInfo(useCases.getOwnUserUuid())

			_sharedState.value = ProfileState(
				username = info.username,
				biography = info.biography,
				profilePicBytes = info.profilePicBytes,
			)

			_privateProfileState.value = _privateProfileState.value?.copy(
				createdAt = info.createdAt,
				isLoading = false,
			)
		}
	}
}
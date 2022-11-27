package com.elian.computeit.feature_profile.presentation.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.domain.states.TextFieldState
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.feature_profile.domain.use_case.GetProfileInfo
import com.elian.computeit.feature_profile.domain.use_case.ValidateProfile
import com.elian.computeit.feature_profile.presentation.edit_profile.EditProfileAction.*
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
class EditProfileViewModel @Inject constructor(
    val getProfileInfo: GetProfileInfo,
    private val validateProfile: ValidateProfile,
) : ViewModel()
{
    private val _eventFlow = Channel<EditProfileEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private val _usernameState = MutableStateFlow(TextFieldState())
    val usernameState = _usernameState.asStateFlow()

    private lateinit var _biography: String


    fun onAction(action: EditProfileAction)
    {
        when (action)
        {
            is EnterUsername  -> _usernameState.update { it.copy(text = action.value, error = null) }
            is EnterBiography -> _biography = action.value
            is Save           -> viewModelScope.launch()
            {
                validateProfile(
                    username = _usernameState.value.text,
                    biography = _biography,
                ).also { result ->

                    _usernameState.update { it.copy(error = result.usernameError) }

                    when (result.resource)
                    {
                        is Resource.Error   -> _eventFlow.send(OnShowErrorMessage(result.resource.uiText ?: UiText.unknownError()))
                        is Resource.Success -> _eventFlow.send(OnSave)
                        else                -> Unit
                    }
                }
            }
        }
    }
}
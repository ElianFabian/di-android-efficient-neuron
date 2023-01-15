package com.elian.computeit.feature_auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.feature_auth.domain.params.RegisterParams
import com.elian.computeit.feature_auth.domain.use_case.RegisterUseCase
import com.elian.computeit.feature_auth.presentation.register.RegisterAction.*
import com.elian.computeit.feature_auth.presentation.register.RegisterEvent.OnRegister
import com.elian.computeit.feature_auth.presentation.register.RegisterEvent.OnShowErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
	private val register: RegisterUseCase,
) : ViewModel()
{
	private val _state = MutableStateFlow(RegisterState())
	val state = _state.asStateFlow()

	private val _eventFlow = Channel<RegisterEvent>()
	val eventFlow = _eventFlow.receiveAsFlow()


	fun onAction(action: RegisterAction)
	{
		when (action)
		{
			is EnterUsername        -> _state.update { it.copy(username = action.value, usernameError = null) }
			is EnterPassword        -> _state.update { it.copy(password = action.value, passwordError = null) }
			is EnterConfirmPassword -> _state.update { it.copy(confirmPassword = action.value, confirmPasswordError = null) }
			is Register             -> viewModelScope.launch()
			{
				_state.update { it.copy(isLoading = true) }

				register(RegisterParams(
					username = _state.value.username,
					password = _state.value.password,
					confirmPassword = _state.value.confirmPassword,
				)).also { result ->

					_state.value = _state.value.copy(
						usernameError = result.usernameError,
						passwordError = result.passwordError,
						confirmPasswordError = result.confirmPasswordError,
					)

					when (val resource = result.resource)
					{
						is Resource.Error   -> _eventFlow.send(OnShowErrorMessage(resource.uiText ?: UiText.unknownError()))
						is Resource.Success -> _eventFlow.send(OnRegister)
						else                -> Unit
					}
				}

				_state.update { it.copy(isLoading = false) }
			}
		}
	}
}
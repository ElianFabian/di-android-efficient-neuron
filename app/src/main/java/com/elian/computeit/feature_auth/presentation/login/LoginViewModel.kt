package com.elian.computeit.feature_auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.feature_auth.domain.params.LoginParams
import com.elian.computeit.feature_auth.domain.use_case.LoginUseCase
import com.elian.computeit.feature_auth.presentation.login.LoginAction.*
import com.elian.computeit.feature_auth.presentation.login.LoginEvent.OnLogin
import com.elian.computeit.feature_auth.presentation.login.LoginEvent.OnShowErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
	private val login: LoginUseCase,
) : ViewModel() {

	private val _state = MutableStateFlow(LoginState())
	val state = _state.asStateFlow()

	private val _eventFlow = Channel<LoginEvent>()
	val eventFlow = _eventFlow.receiveAsFlow()


	fun onAction(action: LoginAction) {
		when (action) {
			is EnterUsername -> _state.update { it.copy(username = action.value, usernameError = null) }
			is EnterPassword -> _state.update { it.copy(password = action.value, passwordError = null) }
			is Login         -> viewModelScope.launch {
				_state.update { it.copy(isLoading = true) }

				login(
					LoginParams(
						username = _state.value.username,
						password = _state.value.password,
					)
				).also { result ->

					_state.value = _state.value.copy(
						usernameError = result.usernameError,
						passwordError = result.passwordError,
					)

					when (val resource = result.resource) {
						is Resource.Error   -> _eventFlow.send(OnShowErrorMessage(resource.uiText ?: UiText.unknownError()))
						is Resource.Success -> _eventFlow.send(OnLogin)
						else                -> Unit
					}
				}

				_state.update { it.copy(isLoading = false) }
			}
		}
	}
}
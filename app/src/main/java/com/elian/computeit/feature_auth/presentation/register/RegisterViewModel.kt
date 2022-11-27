package com.elian.computeit.feature_auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.domain.states.TextFieldState
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.feature_auth.domain.use_case.Register
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
    private val register: Register,
) : ViewModel()
{
    private val _eventFlow = Channel<RegisterEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    private val _usernameState = MutableStateFlow(TextFieldState())
    val usernameState = _usernameState.asStateFlow()

    private val _passwordState = MutableStateFlow(TextFieldState())
    val passwordState = _passwordState.asStateFlow()

    private val _confirmPasswordState = MutableStateFlow(TextFieldState())
    val confirmPasswordState = _confirmPasswordState.asStateFlow()


    fun onAction(action: RegisterAction)
    {
        when (action)
        {
            is RegisterAction.EnterUsername        -> _usernameState.update { it.copy(text = action.value, error = null) }
            is RegisterAction.EnterPassword        -> _passwordState.update { it.copy(text = action.value, error = null) }
            is RegisterAction.EnterConfirmPassword -> _confirmPasswordState.update { it.copy(text = action.value, error = null) }
            is RegisterAction.Register             -> viewModelScope.launch()
            {
                _loadingState.value = true

                register(
                    username = _usernameState.value.text,
                    password = _passwordState.value.text,
                    confirmPassword = _confirmPasswordState.value.text
                ).also { result ->

                    _usernameState.update { it.copy(error = result.usernameError) }
                    _passwordState.update { it.copy(error = result.passwordError) }
                    _confirmPasswordState.update { it.copy(error = result.confirmPasswordError) }

                    when (result.resource)
                    {
                        is Resource.Error   -> _eventFlow.send(OnShowErrorMessage(result.resource.uiText ?: UiText.unknownError()))
                        is Resource.Success -> _eventFlow.send(RegisterEvent.OnRegister)
                        else                -> Unit
                    }
                }

                _loadingState.value = false
            }
        }
    }
}
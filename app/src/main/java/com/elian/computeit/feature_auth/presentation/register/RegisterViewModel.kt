package com.elian.computeit.feature_auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.domain.states.TextFieldState
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.feature_auth.domain.use_case.RegisterUseCase
import com.elian.computeit.feature_auth.presentation.register.RegisterAction.*
import com.elian.computeit.feature_auth.presentation.register.RegisterEvent.OnShowErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val register: RegisterUseCase,
) : ViewModel()
{
    private val _eventFlow = MutableSharedFlow<RegisterEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

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
            is EnterUsername        -> _usernameState.update { it.copy(text = action.value, error = null) }
            is EnterPassword        -> _passwordState.update { it.copy(text = action.value, error = null) }
            is EnterConfirmPassword -> _confirmPasswordState.update { it.copy(text = action.value, error = null) }
            is Register             -> viewModelScope.launch()
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
                        is Resource.Error   -> _eventFlow.emit(OnShowErrorMessage(result.resource.uiText ?: UiText.unknownError()))
                        is Resource.Success -> _eventFlow.emit(RegisterEvent.OnRegister)
                        else                -> Unit
                    }
                }

                _loadingState.value = false
            }
        }
    }
}
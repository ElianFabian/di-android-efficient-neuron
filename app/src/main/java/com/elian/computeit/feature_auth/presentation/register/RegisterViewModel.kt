package com.elian.computeit.feature_auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.domain.states.TextFieldState
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.core.util.constants.EXTRA_USERNAME
import com.elian.computeit.core.util.constants.EXTRA_PASSWORD
import com.elian.computeit.feature_auth.domain.use_case.RegisterUseCase
import com.elian.computeit.feature_auth.presentation.register.RegisterAction.*
import com.elian.computeit.feature_auth.presentation.register.RegisterEvent.OnShowErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
            is EnterUsername        -> _usernameState.value = _usernameState.value.copy(text = action.value, error = null)
            is EnterPassword        -> _passwordState.value = _passwordState.value.copy(text = action.value, error = null)
            is EnterConfirmPassword -> _confirmPasswordState.value = _confirmPasswordState.value.copy(text = action.value, error = null)
            is Register             -> viewModelScope.launch()
            {
                _loadingState.value = true

                register(
                    username = _usernameState.value.text,
                    password = _passwordState.value.text,
                    confirmPassword = _confirmPasswordState.value.text
                ).also()
                {
                    _usernameState.value = _usernameState.value.copy(error = it.usernameError)
                    _passwordState.value = _passwordState.value.copy(error = it.passwordError)
                    _confirmPasswordState.value = _confirmPasswordState.value.copy(error = it.confirmPasswordError)

                    when (it.result)
                    {
                        is Resource.Error   -> _eventFlow.emit(OnShowErrorMessage(it.result.uiText ?: UiText.unknownError()))
                        is Resource.Success ->
                        {
                            val argsToSend = mapOf(
                                EXTRA_USERNAME to _usernameState.value.text,
                                EXTRA_PASSWORD to _passwordState.value.text,
                            ).toList()

                            _eventFlow.emit(RegisterEvent.OnRegister(args = argsToSend))
                        }

                        else                -> Unit
                    }
                }

                _loadingState.value = false
            }
        }
    }
}
package com.elian.computeit.feature_auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.domain.states.TextFieldState
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.feature_auth.domain.use_case.RegisterUseCase
import com.elian.computeit.feature_auth.presentation.register.RegisterAction.*
import com.elian.computeit.feature_auth.presentation.register.RegisterEvent.OnRegister
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
    private val registerUseCase: RegisterUseCase,
) : ViewModel()
{
    private val _eventFlow = MutableSharedFlow<RegisterEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    private val _emailState = MutableStateFlow(TextFieldState())
    val emailState = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow(TextFieldState())
    val passwordState = _passwordState.asStateFlow()

    private val _confirmPasswordState = MutableStateFlow(TextFieldState())
    val confirmPasswordState = _confirmPasswordState.asStateFlow()


    fun onAction(action: RegisterAction)
    {
        when (action)
        {
            is EnterEmail           -> _emailState.value = _emailState.value.copy(text = action.value, error = null)
            is EnterPassword        -> _passwordState.value = _passwordState.value.copy(text = action.value, error = null)
            is EnterConfirmPassword -> _confirmPasswordState.value = _confirmPasswordState.value.copy(text = action.value, error = null)
            is Register             -> viewModelScope.launch()
            {
                _emailState.value = _emailState.value.copy(error = null)
                _passwordState.value = _passwordState.value.copy(error = null)
                _confirmPasswordState.value = _confirmPasswordState.value.copy(error = null)

                register(
                    email = _emailState.value.text,
                    password = _passwordState.value.text,
                    confirmPassword = _confirmPasswordState.value.text
                )
            }
        }
    }


    private suspend fun register(email: String, password: String, confirmPassword: String)
    {
        _loadingState.value = true

        registerUseCase(
            email = email,
            password = password,
            confirmPassword = confirmPassword
        ).also()
        {
            _emailState.value = _emailState.value.copy(error = it.emailError)
            _passwordState.value = _passwordState.value.copy(error = it.passwordError)
            _confirmPasswordState.value = _confirmPasswordState.value.copy(error = it.confirmPasswordError)

            when (it.result)
            {
                is Resource.Error   -> _eventFlow.emit(
                    OnShowErrorMessage(it.result.uiText ?: UiText.unknownError())
                )
                is Resource.Success -> _eventFlow.emit(OnRegister)

                else                -> Unit
            }
        }

        _loadingState.value = false
    }
}
package com.elian.computeit.feature_auth.presentation.register

import androidx.lifecycle.ViewModel
import com.elian.computeit.core.domain.states.StandardTextFieldState
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.feature_auth.domain.use_case.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _emailState = MutableStateFlow(StandardTextFieldState())
    val emailState = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow(StandardTextFieldState())
    val passwordState = _passwordState.asStateFlow()

    private val _confirmPasswordState = MutableStateFlow(StandardTextFieldState())
    val confirmPasswordState = _confirmPasswordState.asStateFlow()


    suspend fun onAction(action: RegisterAction) = when (action)
    {
        is RegisterAction.ReceivedEmail           ->
        {
            _emailState.value = _emailState.value.copy(text = action.value)
        }
        is RegisterAction.ReceivedPassword        ->
        {
            _passwordState.value = _passwordState.value.copy(text = action.value)
        }
        is RegisterAction.ReceivedConfirmPassword ->
        {
            _confirmPasswordState.value = _confirmPasswordState.value.copy(text = action.value)
        }
        is RegisterAction.Register                ->
        {
            register(
                email = _emailState.value.text,
                password = _passwordState.value.text,
                confirmPassword = _confirmPasswordState.value.text
            )
        }
    }


    private suspend fun register(email: String, password: String, confirmPassword: String)
    {
        _loadingState.value = true

        registerUseCase(
            email = email,
            password = password,
            confirmPassword = confirmPassword
        ).apply()
        {
            _emailState.value = _emailState.value.copy(error = emailError)
            _passwordState.value = _passwordState.value.copy(error = passwordError)
            _confirmPasswordState.value = _confirmPasswordState.value.copy(error = confirmPasswordError)

            when (result)
            {
                is Resource.Error   -> _eventFlow.emit(
                    RegisterEvent.ShowErrorMessage(result.uiText ?: UiText.unknownError())
                )
                is Resource.Success -> _eventFlow.emit(RegisterEvent.Register)

                else                -> Unit
            }
        }

        _loadingState.value = false
    }
}
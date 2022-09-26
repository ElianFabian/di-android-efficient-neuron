package com.elian.computeit.feature_auth.presentation.login

import androidx.lifecycle.ViewModel
import com.elian.computeit.feature_auth.domain.use_case.LoginUseCase
import com.elian.computeit.core.domain.states.StandardTextFieldState
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel()
{
    private val _eventFlow = MutableSharedFlow<LoginEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _loginEvent = MutableSharedFlow<LoginAction>()
    val loginAction = _loginEvent.asSharedFlow()

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _emailState = MutableStateFlow(StandardTextFieldState())
    val emailState = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow(StandardTextFieldState())
    val passwordState = _passwordState.asStateFlow()


    suspend fun onAction(action: LoginAction) = when (action)
    {
        is LoginAction.EnteredEmail    ->
        {
            _emailState.emit(_emailState.value.copy(text = action.value))
        }
        is LoginAction.EnteredPassword ->
        {
            _passwordState.emit(_passwordState.value.copy(text = action.value))
        }
        is LoginAction.Login           ->
        {
            login(
                email = _emailState.value.text,
                password = _passwordState.value.text
            )
        }
    }

    private suspend fun login(email: String, password: String)
    {
        _loginState.value = _loginState.value.copy(isLoading = true)

        loginUseCase(
            email = email,
            password = password
        ).apply()
        {
            if (emailError == null)
            {
                _emailState.value = _emailState.value.copy(error = null)
            }
            else _emailState.value = _emailState.value.copy(error = emailError)

            if (passwordError == null)
            {
                _passwordState.value = _passwordState.value.copy(error = null)
            }
            else _passwordState.value = _passwordState.value.copy(error = passwordError)

            when (result)
            {
                is Resource.Error   -> _eventFlow.emit(
                    LoginEvent.ShowErrorMessage(result.uiText ?: UiText.unknownError())
                )
                is Resource.Success -> _eventFlow.emit(LoginEvent.Login)

                else                -> Unit
            }
        }

        _loginState.value = _loginState.value.copy(isLoading = false)
    }
}
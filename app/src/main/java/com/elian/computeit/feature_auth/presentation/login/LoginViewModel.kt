package com.elian.computeit.feature_auth.presentation.login

import androidx.lifecycle.ViewModel
import com.elian.computeit.core.domain.states.StandardTextFieldState
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.feature_auth.domain.use_case.LoginUseCase
import com.elian.computeit.feature_auth.domain.use_case.SaveUserEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val saveUserEmailUseCase: SaveUserEmailUseCase
) : ViewModel()
{
    private val _eventFlow = MutableSharedFlow<LoginEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    private val _emailState = MutableStateFlow(StandardTextFieldState())
    val emailState = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow(StandardTextFieldState())
    val passwordState = _passwordState.asStateFlow()


    suspend fun onAction(action: LoginAction) = when (action)
    {
        is LoginAction.EnteredEmail    ->
        {
            _emailState.value = _emailState.value.copy(text = action.value)
        }
        is LoginAction.EnteredPassword ->
        {
            _passwordState.value = _passwordState.value.copy(text = action.value)
        }
        is LoginAction.Login           ->
        {
            login(
                email = _emailState.value.text,
                password = _passwordState.value.text
            )
        }
    }
    
    suspend fun saveUserEmail(email: String) = saveUserEmailUseCase(email)

    private suspend fun login(email: String, password: String)
    {
        _loadingState.value = true

        loginUseCase(
            email = email,
            password = password
        ).apply()
        {
            _emailState.value = _emailState.value.copy(error = emailError)
            _passwordState.value = _passwordState.value.copy(error = passwordError)

            when (result)
            {
                is Resource.Error   -> _eventFlow.emit(
                    LoginEvent.ShowErrorMessage(result.uiText ?: UiText.unknownError())
                )
                is Resource.Success -> _eventFlow.emit(LoginEvent.Login)

                else                -> Unit
            }
        }

        _loadingState.value = false
    }
}
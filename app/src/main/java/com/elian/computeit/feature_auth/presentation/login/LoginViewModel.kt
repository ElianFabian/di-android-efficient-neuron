package com.elian.computeit.feature_auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.domain.states.TextFieldState
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.feature_auth.domain.use_case.LoginUseCase
import com.elian.computeit.feature_auth.presentation.login.LoginAction.*
import com.elian.computeit.feature_auth.presentation.login.LoginEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel()
{
    private val _eventFlow = MutableSharedFlow<LoginEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    private val _emailState = MutableStateFlow(TextFieldState())
    val emailState = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow(TextFieldState())
    val passwordState = _passwordState.asStateFlow()


    fun onAction(action: LoginAction)
    {
        when (action)
        {
            is EnterEmail    ->
            {
                _emailState.value = _emailState.value.copy(text = action.value)
            }
            is EnterPassword ->
            {
                _passwordState.value = _passwordState.value.copy(text = action.value)
            }
            is Login         -> viewModelScope.launch()
            {
                _emailState.value = _emailState.value.copy(error = null)
                _passwordState.value = _passwordState.value.copy(error = null)

                login(
                    email = _emailState.value.text,
                    password = _passwordState.value.text
                )
            }
        }
    }

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
                is Resource.Error   -> _eventFlow.emit(OnShowErrorMessage(result.uiText ?: UiText.unknownError()))
                is Resource.Success -> _eventFlow.emit(OnLogin)

                else                -> Unit
            }
        }

        _loadingState.value = false
    }
}
package com.elian.computeit.feature_auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.domain.states.TextFieldState
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.feature_auth.domain.use_case.LoginUseCase
import com.elian.computeit.feature_auth.presentation.login.LoginAction.*
import com.elian.computeit.feature_auth.presentation.login.LoginEvent.OnLogin
import com.elian.computeit.feature_auth.presentation.login.LoginEvent.OnShowErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val login: LoginUseCase,
) : ViewModel()
{
    private val _eventFlow = MutableSharedFlow<LoginEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    private val _usernameState = MutableStateFlow(TextFieldState())
    val usernameState = _usernameState.asStateFlow()

    private val _passwordState = MutableStateFlow(TextFieldState())
    val passwordState = _passwordState.asStateFlow()


    fun onAction(action: LoginAction)
    {
        when (action)
        {
            is EnterUsername -> _usernameState.update { it.copy(text = action.value, error = null) }
            is EnterPassword -> _passwordState.update { it.copy(text = action.value, error = null) }
            is Login         -> viewModelScope.launch()
            {
                _loadingState.value = true

                login(
                    username = _usernameState.value.text,
                    password = _passwordState.value.text,
                ).also { result ->

                    _usernameState.update { it.copy(error = result.usernameError) }
                    _passwordState.update { it.copy(error = result.passwordError) }

                    when (result.resource)
                    {
                        is Resource.Error   -> _eventFlow.emit(OnShowErrorMessage(result.resource.uiText ?: UiText.unknownError()))
                        is Resource.Success -> _eventFlow.emit(OnLogin)
                        else                -> Unit
                    }
                }

                _loadingState.value = false
            }
        }
    }
}
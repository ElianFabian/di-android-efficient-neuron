package com.elian.computeit.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.feature_auth.domain.use_case.IsUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val isUserLoggedIn: IsUserLoggedInUseCase,
) : ViewModel()
{
    private val _eventFlow = MutableSharedFlow<MainActivityEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun tryLoginUser()
    {
        viewModelScope.launch()
        {
            if (!isUserLoggedIn()) _eventFlow.emit(MainActivityEvent.UserNotRegistered)
        }
    }
}
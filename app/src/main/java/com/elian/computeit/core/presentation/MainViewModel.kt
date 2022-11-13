package com.elian.computeit.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.domain.repository.AppSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settings: AppSettingsRepository,
) : ViewModel()
{
    private val _eventFlow = MutableSharedFlow<MainActivityEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun tryLogin()
    {
        viewModelScope.launch()
        {
            val isUserNotLoggedIn = settings.getUserUuid() == null

            if (!isUserNotLoggedIn) _eventFlow.emit(MainActivityEvent.OnUserNotLoggedIn)
        }
    }
}
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
class MainActivityViewModel @Inject constructor(
    private val settings: AppSettingsRepository,
) : ViewModel()
{
    private val _eventFlow = MutableSharedFlow<MainActivityEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun tryLoginUser()
    {
        viewModelScope.launch()
        {
            val isUserLoggedIn = settings.getCurrentUserUuid() != null

            if (!isUserLoggedIn) _eventFlow.emit(MainActivityEvent.OnUserNotLoggedIn)
        }
    }
}
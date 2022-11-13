package com.elian.computeit.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import com.elian.computeit.core.domain.util.extensions.isUserLoggedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appRepository: LocalAppDataRepository,
) : ViewModel()
{
    private val _eventFlow = MutableSharedFlow<MainActivityEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun tryLogin() = viewModelScope.launch()
    {
        if (!appRepository.isUserLoggedIn()) _eventFlow.emit(MainActivityEvent.OnUserNotLoggedIn)
    }
}
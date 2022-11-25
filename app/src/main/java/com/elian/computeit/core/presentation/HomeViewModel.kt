package com.elian.computeit.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.domain.use_case.GetTestListInfoUseCase
import com.elian.computeit.feature_tests.domain.models.TestListInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTestListInfo: GetTestListInfoUseCase,
) : ViewModel()
{
    init
    {
        viewModelScope.launch()
        {
            getTestListInfo().collect()
            {
                _infoState.value = it
                _loadingState.value = false
            }
        }
    }


    private val _infoState = MutableStateFlow<TestListInfo?>(null)
    val infoState = _infoState.asStateFlow()

    private val _loadingState = MutableStateFlow(true)
    val isLoadingState = _loadingState.asStateFlow()
}
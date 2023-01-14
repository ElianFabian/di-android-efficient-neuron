package com.elian.computeit.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.domain.use_case.HomeUseCases
import com.elian.computeit.feature_tests.domain.model.TestListInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
	private val useCases: HomeUseCases,
) : ViewModel()
{
	private val _infoState = MutableStateFlow<TestListInfo?>(null)
	val infoState = _infoState.asStateFlow()

	private val _isLoadingState = MutableStateFlow(true)
	val isLoadingState = _isLoadingState.asStateFlow()


	init
	{
		viewModelScope.launch()
		{
			_isLoadingState.value = true

			_infoState.value = useCases.getTestListInfo(useCases.getOwnUserUuid())

			_isLoadingState.value = false
		}
	}


	fun getTestsPerSpeedRange(rangeLength: Int) = useCases.getTestsPerSpeedRange(rangeLength)
}
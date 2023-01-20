package com.elian.computeit.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.domain.use_case.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
	private val useCases: HomeUseCases,
) : ViewModel()
{
	private val _state = MutableStateFlow(HomeState())
	val state = _state.asStateFlow()


	fun fetchInfo()
	{
		viewModelScope.launch()
		{
			if (!useCases.getTestListInfo.isDataCached)
			{
				_state.update { it.copy(isLoading = true) }
			}

			val info = useCases.getTestListInfo(useCases.getOwnUserUuid())

			_state.value = _state.value.copy(
				info = info,
				isLoading = false,
			)
		}
	}

	fun getListOfTestsPerSpeedRange(rangeLength: Int): List<Int> = useCases.getListOfTestsPerSpeedRange(rangeLength)
}
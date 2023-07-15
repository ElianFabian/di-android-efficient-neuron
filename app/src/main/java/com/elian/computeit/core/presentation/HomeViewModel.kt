package com.elian.computeit.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.domain.models.TestListInfo
import com.elian.computeit.core.domain.use_case.HomeUseCases
import com.elian.computeit.feature_tests.domain.args.TestDetailsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
	private val useCases: HomeUseCases,
) : ViewModel() {

	private val _eventFlow = MutableSharedFlow<HomeEvent>()
	val eventFlow = _eventFlow.asSharedFlow()

	private val _state = MutableStateFlow(HomeState())
	val state = _state.asStateFlow()

	private val _screenScrollY = MutableStateFlow(0)
	val screenScrollY = _screenScrollY.asStateFlow()

//	private val _testsHistoryChart = MutableStateFlow(HomeTestsHistoryChartState())
//	val testsHistoryChart = _testsHistoryChart.asStateFlow()

	private val _info = MutableStateFlow<TestListInfo?>(null)
	val info = _info.asStateFlow()

	private val _testCountPerSpeedRange = MutableStateFlow(emptyList<Int>())
	val testCountPerSpeedRange = _testCountPerSpeedRange.asStateFlow()


	init {
		init()
	}


	fun onAction(action: HomeAction) {
		when (action) {
			is HomeAction.SelectTest           -> {
				if (_state.value.selectedTestIndex != action.testIndex) {
					_state.update {
						it.copy(selectedTestIndex = action.testIndex)
					}
				}
			}
			is HomeAction.UnSelectTest         -> {
				_state.update {
					it.copy(selectedTestIndex = -1)
				}
			}
			is HomeAction.GoToTestDetail       -> {

				val selectedTestInfo = _info.value?.listOfTestInfo?.getOrNull(
					_state.value.selectedTestIndex
				)
				
				println("$$$$ selectedTest = ${viewModelScope.isActive} || ${_state.value.selectedTestIndex} || $selectedTestInfo")

				if (selectedTestInfo != null) {
					viewModelScope.launch {
						println("$$$$ send event")
						_eventFlow.emit(
							HomeEvent.OnGoToTestDetail(
								TestDetailsArgs(testInfo = selectedTestInfo)
							)
						)
					}
				}
			}
			is HomeAction.ChangeRangeLength    -> {
				val newTestCountPerSpeedRange = useCases.getTestCountPerSpeedRange(action.rangeLength)

				_testCountPerSpeedRange.value = newTestCountPerSpeedRange

				_state.update {
					it.copy(speedRangeLength = action.rangeLength)
				}
			}
			is HomeAction.ChangeVerticalScroll -> {
				_screenScrollY.value = action.scrollY
			}
		}
	}


	private fun fetchInfo() {
		viewModelScope.launch {
			if (!useCases.getTestListInfo.isDataCached) {
				_state.update { it.copy(isLoading = true) }
			}

			val info = useCases.getTestListInfo(useCases.getOwnUserUuid())

			_info.value = info
			_testCountPerSpeedRange.value = info.testsPerSpeedRange

			_state.value = _state.value.copy(
				speedRangeLength = info.speedRangeLength,
				isLoading = false,
			)
		}
	}

	private fun init() {
		fetchInfo()
	}
}
package com.elian.computeit.feature_tests.presentation.test_configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.domain.models.OperationType
import com.elian.computeit.core.domain.models.Range
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.feature_tests.domain.args.TestArgs
import com.elian.computeit.feature_tests.domain.params.ValidateConfigurationParams
import com.elian.computeit.feature_tests.domain.use_case.ValidateConfigurationUseCase
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationAction.*
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationEvent.OnShowErrorMessage
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationEvent.OnStartTest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestConfigurationViewModel @Inject constructor(
	private val validateConfiguration: ValidateConfigurationUseCase,
) : ViewModel() {

	private val _state = MutableStateFlow(TestConfigurationState())
	val state = _state.asStateFlow()

	private val _eventFlow = Channel<TestConfigurationEvent>()
	val eventFlow = _eventFlow.receiveAsFlow()


	fun onAction(action: TestConfigurationAction) {
		when (action) {
			is SelectOperationType  -> _state.update { it.copy(selectedOperation = OperationType.fromSymbol(action.symbol)) }
			is EnterStartOfRange    -> _state.update { it.copy(startOfRange = action.value, startOfRangeError = null) }
			is EnterEndOfRange      -> _state.update { it.copy(endOfRange = action.value, endOfRangeError = null) }
			is SwapToFixRangeBounds -> {
				val startOfRange = _state.value.startOfRange ?: 0
				val endOfRange = _state.value.endOfRange ?: 0

				if (startOfRange < endOfRange) return

				_state.value = _state.value.copy(
					startOfRange = endOfRange,
					endOfRange = startOfRange,
				)
			}

			is EnterTime            -> _state.update { it.copy(time = action.value, timeError = null) }
			is StartTest            -> {
				val result = validateConfiguration(
					ValidateConfigurationParams(
						operation = _state.value.selectedOperation,
						startOfRange = _state.value.startOfRange,
						endOfRange = _state.value.endOfRange,
						time = _state.value.time,
					)
				)

				_state.value = _state.value.copy(
					startOfRangeError = result.startOfRangeError,
					endOfRangeError = result.endOfRangeError,
					timeError = result.timeError,
				)

				viewModelScope.launch {
					when (val resource = result.resource) {
						is Resource.Error   -> _eventFlow.send(OnShowErrorMessage(resource.uiText ?: UiText.unknownError()))
						is Resource.Success -> {
							_eventFlow.send(
								OnStartTest(
									args = TestArgs(
										operation = _state.value.selectedOperation,
										range = _state.value.run { Range(startOfRange!!, endOfRange!!) },
										totalTimeInSeconds = _state.value.time!!,
									)
								)
							)
						}
						else                -> Unit
					}
				}
			}
		}
	}
}
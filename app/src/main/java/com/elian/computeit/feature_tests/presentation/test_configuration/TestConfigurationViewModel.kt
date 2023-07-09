package com.elian.computeit.feature_tests.presentation.test_configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.domain.models.Range
import com.elian.computeit.core.domain.models.TestConfigurationResultMessage
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.orUnknownError
import com.elian.computeit.feature_tests.domain.args.TestArgs
import com.elian.computeit.feature_tests.domain.params.ValidateConfigurationParams
import com.elian.computeit.feature_tests.domain.use_case.ValidateConfigurationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestConfigurationViewModel @Inject constructor(
	private val validateConfiguration: ValidateConfigurationUseCase,
) : ViewModel() {

	private val _state = MutableStateFlow(TestConfigurationState())
	val state = _state.asStateFlow()

	private val _eventFlow = MutableSharedFlow<TestConfigurationEvent>()
	val eventFlow = _eventFlow.asSharedFlow()


	fun onAction(action: TestConfigurationAction) {
		when (action) {
			is TestConfigurationAction.SelectOperation   -> _state.update {
				it.copy(selectedOperation = action.operationType)
			}
			is TestConfigurationAction.EnterStartOfRange -> _state.update {
				it.copy(startOfRange = action.value, startOfRangeError = null)
			}
			is TestConfigurationAction.EnterEndOfRange   -> _state.update {
				it.copy(endOfRange = action.value, endOfRangeError = null)
			}
			is TestConfigurationAction.EnterTime         -> _state.update {
				it.copy(timeInSeconds = action.timeInSeconds, timeError = null)
			}
			is TestConfigurationAction.StartTest         -> {
				val result = validateConfiguration(
					ValidateConfigurationParams(
						operation = _state.value.selectedOperation,
						startOfRange = _state.value.startOfRange,
						endOfRange = _state.value.endOfRange,
						timeInSeconds = _state.value.timeInSeconds,
					)
				)

				_state.value = _state.value.copy(
					startOfRangeError = result.startOfRangeError,
					endOfRangeError = result.endOfRangeError,
					timeError = result.timeError,
				)

				viewModelScope.launch {
					when (val resource = result.resource) {
						is Resource.Error   -> {
							val actionForMessage: (() -> Unit)? = when (result.messageInfo) {
								is TestConfigurationResultMessage.RangeValuesAreInverted -> {
									{ swipeToFixRangeBounds() }
								}
								else                                                     -> null
							}

							_eventFlow.emit(
								TestConfigurationEvent.OnShowMessage(
									message = resource.message.orUnknownError(),
									action = actionForMessage,
								)
							)
						}
						is Resource.Success -> {
							_eventFlow.emit(
								TestConfigurationEvent.OnStartTest(
									args = TestArgs(
										operation = _state.value.selectedOperation,
										range = _state.value.run { Range(startOfRange!!, endOfRange!!) },
										totalTimeInSeconds = _state.value.timeInSeconds!!,
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

	private fun swipeToFixRangeBounds() {
		val startOfRange = _state.value.startOfRange ?: 0
		val endOfRange = _state.value.endOfRange ?: 0

		if (startOfRange < endOfRange) return

		_state.value = _state.value.copy(
			startOfRange = endOfRange,
			endOfRange = startOfRange,
		)
	}
}
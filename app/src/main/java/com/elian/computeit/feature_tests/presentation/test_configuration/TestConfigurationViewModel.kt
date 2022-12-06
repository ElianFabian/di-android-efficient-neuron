package com.elian.computeit.feature_tests.presentation.test_configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.domain.models.Range
import com.elian.computeit.core.domain.states.NumericFieldState
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.core.util.constants.EXTRA_OPERATION_NUMBER_RANGE
import com.elian.computeit.core.util.constants.EXTRA_OPERATION_TYPE
import com.elian.computeit.core.util.constants.EXTRA_TEST_TIME_IN_SECONDS
import com.elian.computeit.feature_tests.domain.use_case.ValidateConfiguration
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationAction.*
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationEvent.OnShowErrorMessage
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationEvent.OnStart
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
	private val validateConfiguration: ValidateConfiguration,
) : ViewModel()
{
	private val _eventFlow = Channel<TestConfigurationEvent>()
	val eventFlow = _eventFlow.receiveAsFlow()

	private lateinit var _selectedOperation: Operation

	private val _startState = MutableStateFlow(NumericFieldState<Int>())
	val startState = _startState.asStateFlow()

	private val _endState = MutableStateFlow(NumericFieldState<Int>())
	val endState = _endState.asStateFlow()

	private val _timeState = MutableStateFlow(NumericFieldState<Int>())
	val timeState = _timeState.asStateFlow()


	fun onAction(action: TestConfigurationAction) = viewModelScope.launch()
	{
		when (action)
		{
			is SelectOperationType -> _selectedOperation = Operation.fromSymbol(action.symbol)
			is EnterStart          -> _startState.update { it.copy(number = action.value, error = null) }
			is EnterEnd            -> _endState.update { it.copy(number = action.value, error = null) }
			is EnterTime           -> _timeState.update { it.copy(number = action.value, error = null) }
			is StartTest           ->
			{
				validateConfiguration(
					operation = _selectedOperation,
					start = _startState.value.number,
					end = _endState.value.number,
					time = _timeState.value.number,
				).also { result ->

					_startState.update { it.copy(error = result.startError) }
					_endState.update { it.copy(error = result.endError) }
					_timeState.update { it.copy(error = result.timeError) }

					when (val resource = result.resource)
					{
						is Resource.Error   -> _eventFlow.send(OnShowErrorMessage(resource.uiText ?: UiText.unknownError()))
						is Resource.Success ->
						{
							val argsToSend = mapOf(
								EXTRA_OPERATION_TYPE to _selectedOperation,
								EXTRA_OPERATION_NUMBER_RANGE to Range(_startState.value.number!!, _endState.value.number!!),
								EXTRA_TEST_TIME_IN_SECONDS to _timeState.value.number!!,
							).toList()

							_eventFlow.send(OnStart(args = argsToSend))
						}
						else                -> Unit
					}
				}
			}
		}
	}
}
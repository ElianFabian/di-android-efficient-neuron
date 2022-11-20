package com.elian.computeit.feature_tests.presentation.test_configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.data.symbolToOperation
import com.elian.computeit.core.domain.models.Range
import com.elian.computeit.core.domain.states.NumericFieldState
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.core.util.constants.EXTRA_OPERATION_NUMBER_RANGE
import com.elian.computeit.core.util.constants.EXTRA_OPERATION_TYPE
import com.elian.computeit.core.util.constants.EXTRA_TEST_TIME_IN_SECONDS
import com.elian.computeit.feature_tests.domain.use_case.ValidateConfigurationFieldsUseCase
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationAction.*
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationEvent.OnShowErrorMessage
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationEvent.OnStart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestConfigurationViewModel @Inject constructor(
    private val validateFields: ValidateConfigurationFieldsUseCase,
) : ViewModel()
{
    private val _eventFlow = Channel<TestConfigurationEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private lateinit var _selectedOperation: Operation

    private val _minValueState = MutableStateFlow(NumericFieldState<Int>())
    val minValueState = _minValueState.asStateFlow()

    private val _maxValueState = MutableStateFlow(NumericFieldState<Int>())
    val maxValueState = _maxValueState.asStateFlow()

    private val _timeState = MutableStateFlow(NumericFieldState<Int>())
    val timeState = _timeState.asStateFlow()


    fun onAction(action: TestConfigurationAction) = viewModelScope.launch()
    {
        when (action)
        {
            is SelectOperationType -> _selectedOperation = symbolToOperation[action.symbol]!!
            is EnterTime           -> _timeState.value = _timeState.value.copy(number = action.value, error = null)
            is EnterMinValue       -> _minValueState.value = _minValueState.value.copy(number = action.value, error = null)
            is EnterMaxValue       -> _maxValueState.value = _maxValueState.value.copy(number = action.value, error = null)
            is Start               ->
            {
                validateFields(
                    operation = _selectedOperation,
                    minValue = _minValueState.value.number,
                    maxValue = _maxValueState.value.number,
                    time = _timeState.value.number,
                ).also()
                {
                    _minValueState.value = _minValueState.value.copy(error = it.minValueError)
                    _maxValueState.value = _maxValueState.value.copy(error = it.maxValueError)
                    _timeState.value = _timeState.value.copy(error = it.timeError)

                    when (it.result)
                    {
                        is Resource.Error   -> _eventFlow.send(OnShowErrorMessage(it.result.uiText ?: UiText.unknownError()))
                        is Resource.Success ->
                        {
                            val argsToSend = mapOf(
                                EXTRA_OPERATION_TYPE to _selectedOperation,
                                EXTRA_TEST_TIME_IN_SECONDS to _timeState.value.number!!,
                                EXTRA_OPERATION_NUMBER_RANGE to Range(_minValueState.value.number!!, _maxValueState.value.number!!),
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
package com.elian.computeit.feature_tests.presentation.test_configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.data.util.symbolToOperation
import com.elian.computeit.core.domain.states.NumericFieldState
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.core.util.constants.EXTRA_OPERATION_NUMBER_RANGE
import com.elian.computeit.core.util.constants.EXTRA_OPERATION_TYPE
import com.elian.computeit.core.util.constants.EXTRA_TEST_TIME_IN_SECONDS
import com.elian.computeit.feature_tests.data.models.Range
import com.elian.computeit.feature_tests.domain.use_case.ValidateFieldsUseCase
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationAction.*
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationEvent.OnShowErrorMessage
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationEvent.OnStart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestConfigurationViewModel @Inject constructor(
    private val validateFields: ValidateFieldsUseCase,
) : ViewModel()
{
    private val _eventFlow = MutableSharedFlow<TestConfigurationEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private lateinit var _selectedOperation: Operation

    private val _minValueState = MutableStateFlow(NumericFieldState<Int>())
    val minValueState = _minValueState.asStateFlow()

    private val _maxValueState = MutableStateFlow(NumericFieldState<Int>())
    val maxValueState = _maxValueState.asStateFlow()

    private val _testTimeState = MutableStateFlow(NumericFieldState<Int>())
    val testTimeState = _testTimeState.asStateFlow()


    fun onAction(action: TestConfigurationAction) = viewModelScope.launch()
    {
        when (action)
        {
            is SelectOperationType -> _selectedOperation = symbolToOperation[action.symbol]!!
            is EnterSeconds        -> _testTimeState.value = _testTimeState.value.copy(number = action.seconds)
            is EnterRange          ->
            {
                _minValueState.value = _minValueState.value.copy(number = action.min)
                _maxValueState.value = _maxValueState.value.copy(number = action.max)
            }
            is Start               ->
            {
                validateFields(
                    minValue = _minValueState.value.number,
                    maxValue = _maxValueState.value.number,
                    testTime = _testTimeState.value.number,
                ).also()
                {
                    _minValueState.value = _minValueState.value.copy(error = it.minValueError)
                    _maxValueState.value = _maxValueState.value.copy(error = it.maxValueError)
                    _testTimeState.value = _testTimeState.value.copy(error = it.testTimeError)

                    when (it.result)
                    {
                        is Resource.Error   -> _eventFlow.emit(
                            OnShowErrorMessage(it.result.uiText ?: UiText.unknownError())
                        )
                        is Resource.Success ->
                        {
                            val argsToSend = mapOf(
                                EXTRA_OPERATION_TYPE to _selectedOperation,
                                EXTRA_TEST_TIME_IN_SECONDS to _testTimeState.value.number!!,
                                EXTRA_OPERATION_NUMBER_RANGE to Range(_minValueState.value.number!!, _maxValueState.value.number!!),
                            )

                            _eventFlow.emit(OnStart(args = argsToSend.toList()))
                        }

                        else                -> Unit
                    }
                }
            }
        }
    }
}
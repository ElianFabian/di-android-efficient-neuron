package com.elian.computeit.feature_tests.presentation.test_configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.R
import com.elian.computeit.core.data.util.symbolToOperation
import com.elian.computeit.core.domain.states.NumericFieldState
import com.elian.computeit.core.util.Error
import com.elian.computeit.core.util.UiText
import com.elian.computeit.core.util.constants.EXTRA_OPERATION_NUMBER_RANGE
import com.elian.computeit.core.util.constants.EXTRA_OPERATION_TYPE
import com.elian.computeit.core.util.constants.EXTRA_TEST_COUNT
import com.elian.computeit.core.util.constants.EXTRA_TEST_TIME_IN_SECONDS
import com.elian.computeit.feature_tests.data.models.Range
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationAction.*
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationEvent.OnPlay
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationEvent.OnShowErrorMessage
import com.elian.computeit.feature_tests.presentation.util.ConfigurationError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestConfigurationViewModel @Inject constructor() : ViewModel()
{
    private val argsToSend = mutableMapOf<String, Any>()

    private val _eventFlow = MutableSharedFlow<TestConfigurationEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _errorsState = MutableStateFlow<MutableSet<Error>>(mutableSetOf())
    val testConfigurationErrorState = _errorsState.asStateFlow() as StateFlow<Set<Error>?>

    private val _minValueState = MutableStateFlow(NumericFieldState<Int>())
    val minValueState = _minValueState.asStateFlow()

    private val _maxValueState = MutableStateFlow(NumericFieldState<Int>())
    val maxValueState = _maxValueState.asStateFlow()

    private val _testCountOrTimeState = MutableStateFlow(NumericFieldState<Int>())
    val testCountOrTimeState = _testCountOrTimeState.asStateFlow()


    infix fun onAction(action: TestConfigurationAction)
    {
        when (action)
        {
            is SelectOperationType ->
            {
                val operation = symbolToOperation[action.symbol]!!

                argsToSend[EXTRA_OPERATION_TYPE] = operation
            }
            is EnterSeconds        ->
            {
                _testCountOrTimeState.value = _testCountOrTimeState.value.copy(error = getEmptyErrorOrNull(action.seconds))

                if (action.seconds == null) return

                argsToSend[EXTRA_TEST_TIME_IN_SECONDS] = action.seconds
                argsToSend.remove(EXTRA_TEST_COUNT)
            }
            is EnterTestCount      ->
            {
                _testCountOrTimeState.value = _testCountOrTimeState.value.copy(error = getEmptyErrorOrNull(action.testCount))

                if (action.testCount == null) return

                argsToSend[EXTRA_TEST_COUNT] = action.testCount
                argsToSend.remove(EXTRA_TEST_TIME_IN_SECONDS)
            }
            is EnterRange          ->
            {
                _minValueState.value = _minValueState.value.copy(error = getEmptyErrorOrNull(action.min))
                _maxValueState.value = _maxValueState.value.copy(error = getEmptyErrorOrNull(action.max))

                if (action.min == null || action.max == null) return

                when
                {
                    action.min > action.max || action.max < action.min ->
                    {
                        viewModelScope.launch()
                        {
                            _eventFlow.emit(OnShowErrorMessage(UiText.StringResource(
                                R.string.error_range_values_are_inverted
                            )))
                            _errorsState.value.add(ConfigurationError.RangeValuesAreInverted)
                        }
                        return
                    }
                }

                argsToSend[EXTRA_OPERATION_NUMBER_RANGE] = Range(action.min, action.max)
            }
            is Play                ->
            {
                val isThereAnyError = mutableListOf(
                    _minValueState.value.error,
                    _maxValueState.value.error,
                    _testCountOrTimeState.value.error,
                ).run {
                    addAll(_errorsState.value)
                    any { it != null }
                }

                _errorsState.value.clear()

                if (isThereAnyError) return

                viewModelScope.launch()
                {
                    _eventFlow.emit(OnPlay(args = argsToSend.toList()))
                }
            }
        }
    }

    private fun getEmptyErrorOrNull(value: Int?) = if (value == null) ConfigurationError.ValueEmpty else null
}
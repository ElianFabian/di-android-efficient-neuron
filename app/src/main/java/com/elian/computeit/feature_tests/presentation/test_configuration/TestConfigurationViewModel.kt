package com.elian.computeit.feature_tests.presentation.test_configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.data.util.symbolToOperation
import com.elian.computeit.core.domain.states.NumericFieldState
import com.elian.computeit.core.util.EXTRA_OPERATION_NUMBER_RANGE
import com.elian.computeit.core.util.EXTRA_OPERATION_TYPE
import com.elian.computeit.core.util.EXTRA_TEST_COUNT
import com.elian.computeit.core.util.EXTRA_TEST_TIME_IN_SECONDS
import com.elian.computeit.feature_auth.presentation.util.AuthError
import com.elian.computeit.feature_tests.data.models.Range
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationAction.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestConfigurationViewModel @Inject constructor() : ViewModel()
{
    private val argsToSend = mutableMapOf<String, Any>()

    private var isThereAnyError = false

    private val _eventFlow = MutableSharedFlow<TestConfigurationEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _minValueState = MutableStateFlow(NumericFieldState<Int>())
    val minValueState = _minValueState.asStateFlow()

    private val _maxValueState = MutableStateFlow(NumericFieldState<Int>())
    val maxValueState = _maxValueState.asStateFlow()

    private val _testCountOrTimeState = MutableStateFlow(NumericFieldState<Int>())
    val testCountOrTimeState = _testCountOrTimeState.asStateFlow()


    fun onAction(action: TestConfigurationAction)
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
                if (action.seconds == null) return

                _testCountOrTimeState.value = _testCountOrTimeState.value.copy(number = action.seconds)

                argsToSend[EXTRA_TEST_TIME_IN_SECONDS] = action.seconds
                argsToSend.remove(EXTRA_TEST_COUNT)
            }
            is EnterTestCount      ->
            {
                if (action.testCount == null) return

                _testCountOrTimeState.value = _testCountOrTimeState.value.copy(number = action.testCount)

                argsToSend[EXTRA_TEST_COUNT] = action.testCount
                argsToSend.remove(EXTRA_TEST_TIME_IN_SECONDS)
            }
            is EnterRange          ->
            {
                if (action.min == null || action.max == null) return

                _minValueState.value = _minValueState.value.copy(number = action.min)
                _maxValueState.value = _maxValueState.value.copy(number = action.max)

                argsToSend[EXTRA_OPERATION_NUMBER_RANGE] = Range(action.min, action.max)
            }
            is Play                ->
            {
                isThereAnyError = false

                _minValueState.trySetError()
                _maxValueState.trySetError()
                _testCountOrTimeState.trySetError()

                if (isThereAnyError) return

                viewModelScope.launch()
                {
                    _eventFlow.emit(TestConfigurationEvent.OnPlay(args = argsToSend.toList()))
                }
            }
        }
    }

    private fun MutableStateFlow<NumericFieldState<Int>>.trySetError()
    {
        val error = if (value.number == null) AuthError.ValueEmpty else null

        value = value.copy(error = error)

        isThereAnyError = error != null
    }
}
package com.elian.computeit.feature_tests.presentation.test

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.domain.util.CountDownTimer
import com.elian.computeit.core.domain.util.TimerEvent
import com.elian.computeit.core.util.EXTRA_OPERATION_NUMBER_RANGE
import com.elian.computeit.core.util.EXTRA_OPERATION_TYPE
import com.elian.computeit.core.util.EXTRA_TEST_TIME_IN_SECONDS
import com.elian.computeit.core.util.extensions.append
import com.elian.computeit.core.util.extensions.clampLength
import com.elian.computeit.core.data.Operation
import com.elian.computeit.feature_tests.data.models.Range
import com.elian.computeit.feature_tests.data.models.TestData
import com.elian.computeit.feature_tests.presentation.test.TestAction.*
import com.elian.computeit.feature_tests.presentation.util.getRandomPairOfNumbers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val countDownTimer: CountDownTimer,
    savedState: SavedStateHandle,
) : ViewModel()
{
    companion object
    {
        private const val countDownInterval = 1L
    }


    val millisInFuture = savedState.get<Int>(EXTRA_TEST_TIME_IN_SECONDS)?.let { it * 1_000L } ?: 20_000L
    private val range = savedState.get<Range>(EXTRA_OPERATION_NUMBER_RANGE) ?: error("Range value expected.")
    private val operation = savedState.get<Operation>(EXTRA_OPERATION_TYPE) ?: error("Operation value expected")

    init
    {

        initializeTimer()

        countDownTimer.setCoroutineScope(viewModelScope)

        viewModelScope.launch()
        {
            countDownTimer.timerEvent.collect()
            {
                when (it)
                {
                    is TimerEvent.OnTick   ->
                    {
                        _millisUntilFinishState.value = it.millisUntilFinished
                        _eventFlow.emit(TestEvent.OnTimerTick(it.millisUntilFinished))
                    }
                    is TimerEvent.OnFinish -> _eventFlow.emit(TestEvent.OnTimerFinish)

                    else                   -> Unit
                }
            }
        }
    }


    private val _testDataList = mutableListOf<TestData>()

    private val _eventFlow = MutableSharedFlow<TestEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _resultState = MutableStateFlow(0)
    val resultState = _resultState.asStateFlow()

    private val _pairOfNumbersState = MutableStateFlow(getRandomPairOfNumbers(range.min, range.max))
    val pairOfNumbersState = _pairOfNumbersState.asStateFlow()

    private val _millisUntilFinishState = MutableStateFlow(millisInFuture)
    private val _millisSinceStart get() = millisInFuture - _millisUntilFinishState.value


    fun onAction(action: TestAction)
    {
        when (action)
        {
            is EnterNumber ->
            {
                _resultState.value = _resultState.value
                    .append(action.value)
                    .clampLength(maxLength = 8)
            }
            is ClearInput  ->
            {
                _resultState.value = 0
            }
            is NextTest      ->
            {
                val data = TestData(
                    operation = operation.symbol,
                    pairOfNumbers = _pairOfNumbersState.value,
                    insertedResult = _resultState.value,
                    correctResult = operation(_pairOfNumbersState.value),
                    millisSinceStart = _millisSinceStart
                )

                println("------$data")

                _testDataList.add(data)

                _pairOfNumbersState.value = getRandomPairOfNumbers(range.min, range.max)

                _resultState.value = 0
            }
        }
    }

    fun startTimer() = countDownTimer.start()

    private fun initializeTimer()
    {
        countDownTimer.initialize(
            millisInFuture = millisInFuture,
            countDownInterval = countDownInterval
        )
    }
}
package com.elian.computeit.feature_tests.presentation.test

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.domain.util.count_down_timer.CountDownTimer
import com.elian.computeit.core.domain.util.count_down_timer.CountDownTimerEvent
import com.elian.computeit.core.util.EXTRA_OPERATION_TYPE
import com.elian.computeit.core.util.extensions.append
import com.elian.computeit.core.util.extensions.clampLength
import com.elian.computeit.feature_tests.domain.models.Operation
import com.elian.computeit.feature_tests.domain.models.TestData
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
    val millisInFuture = savedState.get<Long>("") ?: 20_000L
    val countDownInterval = savedState.get<Long>("") ?: 1L
    
    init
    {
        countDownTimer.setCoroutineScope(viewModelScope)

        viewModelScope.launch()
        {
            countDownTimer.timerEvent.collect()
            {
                when (it)
                {
                    is CountDownTimerEvent.Ticked   -> _eventFlow.emit(TestEvent.TimerTicked(it.millisUntilFinished))
                    is CountDownTimerEvent.Finished -> _eventFlow.emit(TestEvent.TimerFinished)

                    else                            -> Unit
                }
            }
        }
    }


    private val operation = savedState.get<Operation>(EXTRA_OPERATION_TYPE)!!

    private val testDataList = mutableListOf<TestData>()

    private val _eventFlow = MutableSharedFlow<TestEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _resultState = MutableStateFlow(0)
    val resultState = _resultState.asStateFlow()

    private val _pairOfNumbersState = MutableStateFlow(getRandomPairOfNumbers(min = 1, max = 10))
    val pairOfNumbersState = _pairOfNumbersState.asStateFlow()


    fun onAction(action: TestAction)
    {
        when (action)
        {
            is TestAction.EnteredNumber ->
            {
                _resultState.value = _resultState.value
                    .append(action.value)
                    .clampLength(maxLength = 8)
            }
            is TestAction.ClearInput    ->
            {
                _resultState.value = 0
            }
            is TestAction.NextTest      ->
            {
                _resultState.value = 0

                val data = TestData(
                    operation = operation.symbol,
                    pairOfNumbers = _pairOfNumbersState.value,
                    insertedResult = _resultState.value,
                    correctResult = operation(_pairOfNumbersState.value),
                    timeInMillisSinceTestStarted = 1L
                )

                testDataList.add(data)

                _pairOfNumbersState.value = getRandomPairOfNumbers(min = 1, max = 10)
            }
        }
    }

    fun initializeTimer()
    {
        countDownTimer.initialize(
            millisInFuture = millisInFuture,
            countDownInterval = countDownInterval
        )
    }

    fun startTimer() = countDownTimer.start()
}
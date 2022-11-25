package com.elian.computeit.feature_tests.presentation.test

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.domain.models.NumberPair
import com.elian.computeit.core.domain.models.OperationData
import com.elian.computeit.core.domain.models.Range
import com.elian.computeit.core.domain.models.TestData
import com.elian.computeit.core.domain.util.CountDownTimer
import com.elian.computeit.core.domain.util.TimerEvent
import com.elian.computeit.core.util.constants.EXTRA_OPERATION_NUMBER_RANGE
import com.elian.computeit.core.util.constants.EXTRA_OPERATION_TYPE
import com.elian.computeit.core.util.constants.EXTRA_TEST_INFO
import com.elian.computeit.core.util.constants.EXTRA_TEST_TIME_IN_SECONDS
import com.elian.computeit.core.util.extensions.append
import com.elian.computeit.core.util.extensions.clampLength
import com.elian.computeit.feature_tests.domain.models.toTestInfo
import com.elian.computeit.feature_tests.domain.use_case.AddTestDataUseCase
import com.elian.computeit.feature_tests.domain.use_case.GetRandomNumberPairUseCase
import com.elian.computeit.feature_tests.presentation.test.TestAction.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sign

@HiltViewModel
class TestViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val countDownTimer: CountDownTimer,
    private val addTestData: AddTestDataUseCase,
    private val getRandomNumberPair: GetRandomNumberPairUseCase,
) : ViewModel()
{
    companion object
    {
        private const val COUNT_DOWN_INTERVAL = 1L
    }


    val millisInFuture = savedState.get<Int>(EXTRA_TEST_TIME_IN_SECONDS)!! * 1_000L
    private val _range = savedState.get<Range>(EXTRA_OPERATION_NUMBER_RANGE)!!
    private val _operation = savedState.get<Operation>(EXTRA_OPERATION_TYPE)!!

    private val _testDataList = mutableListOf<OperationData>()

    private val _eventFlow = MutableSharedFlow<TestEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _resultState = MutableStateFlow(0)
    val resultState = _resultState.asStateFlow()

    private val _pairOfNumbersState = MutableStateFlow<NumberPair?>(null)
    val pairOfNumbersState = _pairOfNumbersState.asStateFlow()

    private var _millisUntilFinish = millisInFuture
    private val _millisSinceStart get() = millisInFuture - _millisUntilFinish

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
                    is TimerEvent.OnStart  ->
                    {
                        _pairOfNumbersState.value = getRandomNumberPair()
                    }
                    is TimerEvent.OnTick   ->
                    {
                        _millisUntilFinish = it.millisUntilFinished
                        _eventFlow.emit(TestEvent.OnTimerTick(it.millisUntilFinished))
                    }
                    is TimerEvent.OnFinish ->
                    {
                        val testData = TestData(
                            dateInSeconds = System.currentTimeMillis() / 1000,
                            testTimeInSeconds = _millisSinceStart.toInt() / 1000,
                            operationDataList = _testDataList.toList(),
                            range = _range
                        )

                        _eventFlow.emit(TestEvent.OnTimerFinish(
                            args = listOf(EXTRA_TEST_INFO to testData.toTestInfo())
                        ))

                        addTestData(testData)
                    }
                    else                   -> Unit
                }
            }
        }
    }

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
            is ClearInput  -> _resultState.value = 0
            is NextTest    ->
            {
                val expectedResult = _operation(
                    first = _pairOfNumbersState.value!!.first,
                    second = _pairOfNumbersState.value!!.second
                )

                // As there's no negative sign button even if the answer it's negative you can introduce a positive number
                // but when storing the data we save the value with the correct sign
                val sign = sign(expectedResult.toFloat()).toInt()

                val data = OperationData(
                    operation = _operation.symbol,
                    pairOfNumbers = _pairOfNumbersState.value!!,
                    insertedResult = _resultState.value * sign,
                    expectedResult = expectedResult,
                    millisSinceStart = _millisSinceStart
                )

                _testDataList.add(data)

                _pairOfNumbersState.value = getRandomNumberPair(oldPair = _pairOfNumbersState.value)
                _resultState.value = 0
            }
        }
    }

    fun startTimer() = countDownTimer.start()

    private fun initializeTimer()
    {
        countDownTimer.initialize(
            millisInFuture = millisInFuture,
            countDownInterval = COUNT_DOWN_INTERVAL
        )
    }
}
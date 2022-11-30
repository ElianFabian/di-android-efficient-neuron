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
import com.elian.computeit.feature_tests.domain.model.toTestInfo
import com.elian.computeit.feature_tests.domain.use_case.AddTestData
import com.elian.computeit.feature_tests.domain.use_case.GetRandomNumberPair
import com.elian.computeit.feature_tests.presentation.test.TestAction.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sign

@HiltViewModel
class TestViewModel @Inject constructor(
	savedState: SavedStateHandle,
	private val countDownTimer: CountDownTimer,
	private val addTestData: AddTestData,
	private val getRandomNumberPair: GetRandomNumberPair,
) : ViewModel()
{
	private val _totalTimeInMillis = savedState.get<Int>(EXTRA_TEST_TIME_IN_SECONDS)!! * 1_000L
	private val _range = savedState.get<Range>(EXTRA_OPERATION_NUMBER_RANGE)!!
	private val _operation = savedState.get<Operation>(EXTRA_OPERATION_TYPE)!!

	private val _listOfOperationData = mutableListOf<OperationData>()

	private val _eventFlow = Channel<TestEvent>()
	val eventFlow = _eventFlow.receiveAsFlow()

	private val _resultState = MutableStateFlow(0)
	val resultState = _resultState.asStateFlow()

	private val _pairOfNumbersState = MutableStateFlow<NumberPair?>(null)
	val pairOfNumbersState = _pairOfNumbersState.asStateFlow()

	private var _millisUntilFinish = _totalTimeInMillis
	private val _millisSinceStart get() = _totalTimeInMillis - _millisUntilFinish

	init
	{
		countDownTimer.initialize(
			millisInFuture = _totalTimeInMillis,
			countDownInterval = 1L,
			coroutineScope = viewModelScope,
		)

		viewModelScope.launch()
		{
			countDownTimer.timerEventFlow.collect()
			{
				when (it)
				{
					is TimerEvent.OnStart  -> _pairOfNumbersState.value = getRandomNumberPair()
					is TimerEvent.OnTick   ->
					{
						_millisUntilFinish = it.millisUntilFinished
						_eventFlow.send(TestEvent.OnTimerTick(it.millisUntilFinished))
					}
					is TimerEvent.OnFinish ->
					{
						val testData = TestData(
							dateUnix = System.currentTimeMillis() / 1000,
							timeInSeconds = _millisSinceStart.toInt() / 1000,
							listOfOperationData = _listOfOperationData.toList(),
							range = _range
						)

						_eventFlow.send(TestEvent.OnTimerFinish(
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
			is EnterNumber -> _resultState.update { it.append(action.value).clampLength(maxLength = 8) }
			is ClearInput  -> _resultState.value = 0
			is NextTest    ->
			{
				val expectedResult = _operation(
					firstNumber = _pairOfNumbersState.value!!.first,
					secondNumber = _pairOfNumbersState.value!!.second
				)

				// As there's no negative sign button even if the answer it's negative you can introduce a positive number
				// but when storing the data we save the value with the correct sign
				val sign = sign(expectedResult.toFloat()).toInt()

				val data = OperationData(
					operationName = _operation.name,
					pairOfNumbers = _pairOfNumbersState.value!!,
					insertedResult = _resultState.value * sign,
					expectedResult = expectedResult,
					millisSinceStart = _millisSinceStart
				)

				_listOfOperationData.add(data)

				_pairOfNumbersState.value = getRandomNumberPair(oldPair = _pairOfNumbersState.value)
				_resultState.value = 0
			}
		}
	}

	fun startTimer() = countDownTimer.start()
}
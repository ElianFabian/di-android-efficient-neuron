package com.elian.computeit.feature_tests.presentation.test

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.data.toTestInfo
import com.elian.computeit.core.domain.models.NumberPair
import com.elian.computeit.core.domain.models.OperationData
import com.elian.computeit.core.domain.models.TestData
import com.elian.computeit.core.domain.util.CountDownTimer
import com.elian.computeit.core.domain.util.TimerEvent
import com.elian.computeit.core.util.constants.receiveArgs
import com.elian.computeit.core.util.constants.toList
import com.elian.computeit.core.util.extensions.append
import com.elian.computeit.core.util.extensions.clampLength
import com.elian.computeit.core.util.extensions.dropLast
import com.elian.computeit.feature_tests.domain.args.TestArgs
import com.elian.computeit.feature_tests.domain.args.TestDetailsArgs
import com.elian.computeit.feature_tests.domain.use_case.TestUseCases
import com.elian.computeit.feature_tests.presentation.test.TestAction.*
import com.elian.computeit.feature_tests.presentation.test.TestEvent.OnGoToTestDetails
import com.elian.computeit.feature_tests.presentation.test.TestEvent.OnTimerFinish
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
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
	private val useCases: TestUseCases,
) : ViewModel()
{
	private val _args = savedState.receiveArgs<TestArgs>()!!

	private val _eventFlow = Channel<TestEvent>()
	val eventFlow = _eventFlow.receiveAsFlow()

	private val _resultState = MutableStateFlow(0)
	val resultState = _resultState.asStateFlow()

	private val _pairOfNumbersState = MutableStateFlow<NumberPair?>(null)
	val pairOfNumbersState = _pairOfNumbersState.asStateFlow()

	private val _operationSymbolState = MutableStateFlow(_args.operation.symbol)
	val operationSymbolState = _operationSymbolState.asStateFlow()

	private val _range = _args.run { range.min..range.max }

	private val _listOfOperationData = mutableListOf<OperationData>()

	private val _isInfiniteMode = _args.totalTimeInSeconds == 0
	private var _millisSinceStart = 0L

	private val _expectedResult
		get() = _args.operation(
			firstNumber = _pairOfNumbersState.value?.first ?: 0,
			secondNumber = _pairOfNumbersState.value?.second ?: 0,
		)

	// As there's no negative sign button even if the answer it's negative you insert a positive number
	// but when storing the data we save the value with the right sign
	private val _resultSign get() = sign(_expectedResult.toFloat()).toInt()


	init
	{
		initialize()
	}


	fun onAction(action: TestAction)
	{
		when (action)
		{
			is EnterNumber     ->
			{
				_resultState.update { it.append(action.value).clampLength(maxLength = 8) }

				// We automatically add the result if it is correct
				val isInsertedResultCorrect = _resultState.value * _resultSign == _expectedResult
				if (isInsertedResultCorrect)
				{
					addResult()
					nextOperation()
				}
			}
			is RemoveLastDigit -> _resultState.update { it.dropLast() }
			is ClearInput      -> _resultState.value = 0
			is NextOperation   ->
			{
				addResult()
				nextOperation()
			}
			is ForceFinish     -> viewModelScope.launch { finishTest(saveData = false) }
		}
	}

	fun startTimer()
	{
		_pairOfNumbersState.value = useCases.getRandomNumberPairFromOperation(
			operation = _args.operation,
			range = _range,
		)

		countDownTimer.start()
	}


	private fun initialize()
	{
		val countDownInterval = 1L

		countDownTimer.initialize(
			millisInFuture = if (_isInfiniteMode) Long.MAX_VALUE else _args.totalTimeInSeconds * 1_000L,
			countDownInterval = countDownInterval,
			coroutineScope = viewModelScope,
		)

		viewModelScope.launch()
		{
			countDownTimer.timerEventFlow.collect()
			{
				when (it)
				{
					is TimerEvent.OnTick   ->
					{
						_millisSinceStart += countDownInterval

						if (!_isInfiniteMode)
						{
							_eventFlow.send(TestEvent.OnTimerTickInNormalMode(
								millisSinceStart = it.millisSinceStart,
								millisUntilFinished = it.millisUntilFinished,
							))
						}
						else _eventFlow.send(TestEvent.OnTimerTickInInfiniteMode(
							millisSinceStart = _millisSinceStart,
						))
					}
					is TimerEvent.OnFinish -> finishTest()
					else                   -> Unit
				}
			}
		}
	}

	private fun addResult()
	{
		val data = OperationData(
			operationName = _args.operation.name,
			pairOfNumbers = _pairOfNumbersState.value!!,
			insertedResult = _resultState.value * _resultSign,
			millisSinceStart = _millisSinceStart,
		)

		_listOfOperationData.add(data)
	}

	private fun nextOperation()
	{
		_pairOfNumbersState.value = useCases.getRandomNumberPairFromOperation(
			operation = _args.operation,
			range = _range,
			oldPair = _pairOfNumbersState.value,
		)

		_resultState.value = 0
	}

	private suspend fun finishTest(saveData: Boolean = true)
	{
		_eventFlow.send(OnTimerFinish)

		val totalTime = if (_isInfiniteMode) _millisSinceStart else _args.totalTimeInSeconds * 1_000L

		val testData = TestData(
			dateUnix = System.currentTimeMillis(),
			timeInSeconds = totalTime.toInt() / 1000,
			listOfOperationData = _listOfOperationData.toList(),
			range = _args.range
		)

		_eventFlow.send(OnGoToTestDetails(
			args = TestDetailsArgs(
				testInfo = testData.toTestInfo(),
			).toList()
		))

		// This is to avoid the cancellation of the viewModelScope
		MainScope().launch(Dispatchers.IO)
		{
			if (saveData) useCases.addTestData(
				userUuid = useCases.getOwnUserUuid(),
				testData = testData,
			)
		}
	}
}
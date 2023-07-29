package com.elian.computeit.feature_tests.presentation.test

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.data.mapper.toTestInfo
import com.elian.computeit.core.data.model.OperationData
import com.elian.computeit.core.data.model.TestData
import com.elian.computeit.core.domain.util.timer.SimpleTimer
import com.elian.computeit.core.domain.util.timer.Timer
import com.elian.computeit.core.util.constants.arguments
import com.elian.computeit.core.util.extensions.append
import com.elian.computeit.core.util.extensions.clampLength
import com.elian.computeit.core.util.extensions.dropLast
import com.elian.computeit.core.util.extensions.orZero
import com.elian.computeit.feature_tests.domain.args.TestArgs
import com.elian.computeit.feature_tests.domain.args.TestDetailsArgs
import com.elian.computeit.feature_tests.domain.use_case.TestUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sign

@HiltViewModel
class TestViewModel @Inject constructor(
	savedState: SavedStateHandle,
	private val useCases: TestUseCases,
) : ViewModel() {

	private val _args = savedState.arguments<TestArgs>()!!
	private val _range = _args.run { range.min..range.max }


	private val _state = MutableStateFlow(
		TestState(
			operation = _args.operation,
			totalTimeInMillis = _args.totalTimeInSeconds * 1000L,
			range = _args.range,
		)
	)
	val state = _state.asStateFlow()

	private val countDownTimer = SimpleTimer(
		direction = when (_state.value.testType) {
			TestType.FINITE   -> Timer.Direction.COUNT_DOWN
			TestType.INFINITE -> Timer.Direction.COUNT_UP
		},
		initialMillis = when (_state.value.testType) {
			TestType.FINITE   -> _args.totalTimeInSeconds * 1000L
			TestType.INFINITE -> 0L
		},
		minMillis = 0L,
		periodInMillis = 1L,
	)

	val timerMillisState = countDownTimer.millisState
	val formattedTimeState = countDownTimer.millisState
		.map { millis ->
			formatTimeInMillis(millis)
		}.stateIn(
			viewModelScope,
			SharingStarted.WhileSubscribed(5000),
			formatTimeInMillis(_args.totalTimeInSeconds.toLong()),
		)

	private val _eventFlow = MutableSharedFlow<TestEvent>()
	val eventFlow = _eventFlow.asSharedFlow()

	private val _listOfOperationData = mutableListOf<OperationData>()

	private fun getExpectedResult() = _args.operation(
		firstNumber = _state.value.pairOfNumbers?.first.orZero(),
		secondNumber = _state.value.pairOfNumbers?.second.orZero(),
	)


	init {
		init()
	}


	fun onAction(action: TestAction) {
		when (action) {
			is TestAction.StartTest       -> {
				startTest()
			}
			is TestAction.EnterDigit      -> {
				_state.update {
					it.copy(
						insertedResult = it.insertedResult
							.append(action.digit)
							.clampLength(maxLength = 8)
					)
				}

				val expectedResult = getExpectedResult()
				val sign = expectedResult.sign

				val isInsertedResultCorrect = _state.value.insertedResult * sign == expectedResult
				if (isInsertedResultCorrect) {
					addResult()
					nextOperation()
				}
			}
			is TestAction.RemoveLastDigit -> {
				_state.update {
					it.copy(
						insertedResult = it.insertedResult.dropLast()
					)
				}
			}
			is TestAction.ClearInput      -> {
				_state.update {
					it.copy(insertedResult = 0)
				}
			}
			is TestAction.NextOperation   -> {
				addResult()
				nextOperation()
			}
			is TestAction.ForceFinish     -> {
				finishTest()
			}
		}
	}

	private fun startTest() {
		_state.value = _state.value.copy(
			pairOfNumbers = useCases.getRandomNumberPairFromOperation(
				operation = _args.operation,
				range = _range,
			)
		)

		countDownTimer.play()
	}

	private fun addResult() {
		val state = _state.value

		val data = OperationData(
			operationName = _args.operation.name,
			pairOfNumbers = state.pairOfNumbers!!,
			insertedResult = state.insertedResult * getExpectedResult().sign,
			millisSinceStart = when (state.testType) {
				TestType.INFINITE -> timerMillisState.value
				TestType.FINITE   -> state.totalTimeInMillis - timerMillisState.value
			},
		)

		_listOfOperationData.add(data)
	}

	private fun nextOperation() {
		_state.value = _state.value.copy(
			pairOfNumbers = useCases.getRandomNumberPairFromOperation(
				operation = _args.operation,
				range = _range,
				oldPair = _state.value.pairOfNumbers,
			),
			insertedResult = 0,
		)
	}

	private fun finishTest() {
		viewModelScope.launch {
			_eventFlow.emit(TestEvent.OnTimerFinish)

			val testType = _state.value.testType

			val totalTimeInMillis = when (testType) {
				TestType.INFINITE -> timerMillisState.value
				else              -> _state.value.totalTimeInMillis
			}

			val testData = TestData(
				dateUnix = System.currentTimeMillis(),
				range = _args.range,
				timeInSeconds = totalTimeInMillis.toInt() / 1000,
				listOfOperationData = _listOfOperationData.toList(),
			)

			_eventFlow.emit(
				TestEvent.OnGoToTestDetails(
					args = TestDetailsArgs(
						testInfo = testData.toTestInfo(),
					)
				)
			)

			val saveData = testType == TestType.FINITE
			if (saveData) {
				MainScope().launch {
					useCases.addTestData(
						userUuid = useCases.getOwnUserUuid(),
						testData = testData,
					)
				}
			}
		}
	}

	private fun init() {
		viewModelScope.launch {
			countDownTimer.millisState.collectLatest { millis ->

				if (_state.value.testType == TestType.FINITE && millis <= 0) {
					finishTest()
				}
			}
		}
	}

	private fun formatTimeInMillis(millis: Long): String {
		if (_state.value.testType == TestType.INFINITE) return "∞"

		return "%.1f".format(millis / 1000F)
	}
}
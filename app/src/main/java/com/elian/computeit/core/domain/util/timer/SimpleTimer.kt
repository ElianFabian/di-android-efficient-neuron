package com.elian.computeit.core.domain.util.timer

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Suppress("FunctionName")
fun SimpleTimer(
	initialMillis: Long = 0L,
	periodInMillis: Long = 1L,
	minMillis: Long = 0L,
	maxMillis: Long = Long.MAX_VALUE,
	direction: Timer.Direction = Timer.Direction.COUNT_UP,
): Timer = TimerImpl(
	initialMillis = initialMillis,
	periodInMillis = periodInMillis,
	minMillis = minMillis,
	maxMillis = maxMillis,
	direction = direction,
)


private class TimerImpl(
	override var initialMillis: Long = 0L,
	periodInMillis: Long = 1L,
	minMillis: Long = 0L,
	maxMillis: Long = Long.MAX_VALUE,
	direction: Timer.Direction = Timer.Direction.COUNT_UP,
) : Timer {

	private val _millisState = MutableStateFlow(initialMillis)
	override val millisState = _millisState.asStateFlow()

	private val timerState = MutableStateFlow(
		Timer.State(
			initialMillis = initialMillis,
			minMillis = minMillis,
			maxMillis = maxMillis,
			direction = direction,
		)
	)

	private val preciseTicker = object : SimplePreciseTicker(periodInMillis) {
		override fun onTick() {

			val state = timerState.value
			val period = this.periodInMillis

			val positiveOrNegativeInterval = when (state.direction) {
				Timer.Direction.COUNT_UP   -> +period
				Timer.Direction.COUNT_DOWN -> -period
			}

			val newMillis = _millisState.value + positiveOrNegativeInterval

			val areMillisOutOfRange = newMillis < state.minMillis || state.maxMillis < newMillis
			if (areMillisOutOfRange) {
				pause()
				return
			}

			_millisState.value = newMillis
		}
	}

	override val state = timerState.asStateFlow()

	override var periodInMillis: Long = 1L
		set(periodInMillis) {
			preciseTicker.periodInMillis = periodInMillis
			field = periodInMillis
		}

	override var minMillis = minMillis
		set(minMillis) {
			if (_millisState.value > minMillis) {
				preciseTicker.play()
			}

			timerState.update {
				it.copy(minMillis = minMillis)
			}
		}

	override var maxMillis = maxMillis
		set(maMillis) {
			if (maMillis < state.value.maxMillis) {
				preciseTicker.play()
			}

			timerState.update {
				it.copy(maxMillis = maMillis)
			}
		}


	override var direction: Timer.Direction
		get() = timerState.value.direction
		set(direction) {
			val isRunning = state.value.isRunning
			val hasStarted = state.value.hasStarted

			val millis = _millisState.value
			val isUp = direction == Timer.Direction.COUNT_UP
			val isDown = direction == Timer.Direction.COUNT_DOWN
			val canMoveToNewDirection = isUp && millis <= minMillis || isDown && maxMillis <= millis

			timerState.update {
				it.copy(direction = direction)
			}

			if (hasStarted && isRunning && canMoveToNewDirection) {
				preciseTicker.play()
			}
		}


	override fun play() {
		if (!timerState.value.hasStarted) {
			timerState.update {
				it.copy(hasStarted = true)
			}
		}

		timerState.update {
			it.copy(isRunning = true)
		}

		preciseTicker.play()
	}

	override fun pause() {
		timerState.update {
			it.copy(isRunning = false)
		}

		preciseTicker.pause()
	}

	override fun restart() {
		timerState.update {
			it.copy(
				hasStarted = false,
				isRunning = false,
			)
		}

		_millisState.value = timerState.value.initialMillis

		preciseTicker.pause()
	}
}
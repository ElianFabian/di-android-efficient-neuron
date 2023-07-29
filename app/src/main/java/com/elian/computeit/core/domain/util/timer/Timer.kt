package com.elian.computeit.core.domain.util.timer

import kotlinx.coroutines.flow.StateFlow

interface Timer {

	fun play()
	fun pause()
	fun restart()

	val state: StateFlow<State>
	val millisState: StateFlow<Long>

	var initialMillis: Long
	var periodInMillis: Long
	var minMillis: Long
	var maxMillis: Long
	var direction: Direction


	data class State(
		val hasStarted: Boolean = false,
		val isRunning: Boolean = false,
		val initialMillis: Long,
		val minMillis: Long,
		val maxMillis: Long,
		val direction: Direction,
	)

	enum class Direction {
		COUNT_UP,
		COUNT_DOWN,
	}
}
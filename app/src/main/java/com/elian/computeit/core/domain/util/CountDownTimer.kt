package com.elian.computeit.core.domain.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface CountDownTimer
{
	fun initialize(
		millisInFuture: Long,
		countDownInterval: Long,
		coroutineScope: CoroutineScope,
	)

	fun start()
	fun restart()
	fun stop()
	fun resume()

	val timerEventFlow: Flow<TimerEvent>
}

sealed interface TimerEvent
{
	object OnStart : TimerEvent

	data class OnTick(
		val millisSinceStart: Long,
		val millisUntilFinished: Long,
	) : TimerEvent

	object OnRestart : TimerEvent
	object OnStop : TimerEvent
	object OnResume : TimerEvent
	object OnFinish : TimerEvent
}
package com.elian.computeit.core.domain.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow

interface CountDownTimer
{
    fun initialize(millisInFuture: Long, countDownInterval: Long)
    fun start()
    fun restart()
    fun stop()
    fun resume()
    fun setCoroutineScope(coroutineScope: CoroutineScope)

    val timerEvent: SharedFlow<TimerEvent>
    val millisInFuture: Long
    val countDownInterval: Long
}

sealed interface TimerEvent
{
    object OnStart : TimerEvent
    data class OnTick(val millisUntilFinished: Long) : TimerEvent
    object OnRestart : TimerEvent
    object OnStop : TimerEvent
    object OnResume : TimerEvent
    object OnFinish : TimerEvent
}
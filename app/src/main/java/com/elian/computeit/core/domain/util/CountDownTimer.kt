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
    object Started : TimerEvent
    data class Ticked(val millisUntilFinished: Long) : TimerEvent
    object Restarted : TimerEvent
    object Stopped : TimerEvent
    object Resumed : TimerEvent
    object Finished : TimerEvent
}
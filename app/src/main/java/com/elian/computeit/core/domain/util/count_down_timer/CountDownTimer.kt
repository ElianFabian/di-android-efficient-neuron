package com.elian.computeit.core.domain.util.count_down_timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow

interface CountDownTimer
{
    fun initialize(millisInFuture: Long, countDownInterval: Long)
    fun start()
    fun restart()
    fun stop()
    fun setCoroutineScope(coroutineScope: CoroutineScope)
    val timerEvent: SharedFlow<CountDownTimerEvent>
    val millisInFuture: Long
    val countDownInterval: Long
}
package com.elian.computeit.core.domain.util

interface CountDownTimer
{
    fun initialize(millisInFuture: Long, countDownInterval: Long)
    fun start()
    fun restart()
    fun stop()
    val millisInFuture: Long
    val countDownInterval: Long
    var onTick: ((millisUntilFinished: Long) -> Unit)?
    var onFinish: (() -> Unit)?
}
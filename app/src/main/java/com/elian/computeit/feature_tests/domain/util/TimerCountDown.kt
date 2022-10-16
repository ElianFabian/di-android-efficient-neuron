package com.elian.computeit.feature_tests.domain.util

interface TimerCountDown
{
    fun initializeTimer(millisInFuture: Long, countDownInterval: Long)
    fun startTimer()
    fun cancelTimer()
    val millisInFuture: Long
    val countDownInterval: Long
    var onTick: ((millisUntilFinished: Long) -> Unit)?
    var onFinish: (() -> Unit)?
}
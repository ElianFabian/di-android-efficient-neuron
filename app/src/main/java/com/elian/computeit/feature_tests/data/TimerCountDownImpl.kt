package com.elian.computeit.feature_tests.data

import android.os.CountDownTimer
import com.elian.computeit.feature_tests.domain.util.TimerCountDown

class TimerCountDownImpl : TimerCountDown
{
    private lateinit var countDownTimer: CountDownTimer
    private var _millisInFuture = 0L
    private var _countDownInterval = 0L

    override fun initializeTimer(millisInFuture: Long, countDownInterval: Long)
    {
        _millisInFuture = millisInFuture
        _countDownInterval = countDownInterval

        countDownTimer = object : CountDownTimer(millisInFuture, countDownInterval)
        {
            override fun onTick(millisUntilFinished: Long)
            {
                onTick?.invoke(millisUntilFinished)
            }

            override fun onFinish()
            {
                onFinish?.invoke()
            }
        }
    }

    override fun startTimer()
    {
        countDownTimer.start()
    }

    override fun cancelTimer() = countDownTimer.cancel()

    override val millisInFuture: Long get() = _millisInFuture

    override val countDownInterval: Long get() = _countDownInterval

    override var onTick: ((Long) -> Unit)? = null
    override var onFinish: (() -> Unit)? = null
}
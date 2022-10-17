package com.elian.computeit.feature_tests.data

import com.elian.computeit.core.domain.util.CountDownTimer
import com.elian.computeit.core.util.PreciseCountDown

class PreciseCountDownImpl : CountDownTimer
{
    private lateinit var countDownTimer: PreciseCountDown
    private var _millisInFuture = 0L
    private var _countDownInterval = 0L

    override fun initialize(millisInFuture: Long, countDownInterval: Long)
    {
        _millisInFuture = millisInFuture
        _countDownInterval = countDownInterval

        countDownTimer = object : PreciseCountDown(millisInFuture, countDownInterval)
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

    override fun start() = countDownTimer.start()

    override fun restart() = countDownTimer.restart()

    override fun stop() = countDownTimer.stop()

    override val millisInFuture: Long get() = _millisInFuture

    override val countDownInterval: Long get() = _countDownInterval

    override var onTick: ((Long) -> Unit)? = null
    override var onFinish: (() -> Unit)? = null
}
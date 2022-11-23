package com.elian.computeit.core.data.util

import com.elian.computeit.core.domain.util.CountDownTimer
import com.elian.computeit.core.domain.util.TimerEvent
import com.elian.computeit.core.util.PreciseCountDownTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CountDownTimerImpl : CountDownTimer
{
    private lateinit var countDownTimer: PreciseCountDownTimer
    private var _millisInFuture = 0L
    private var _countDownInterval = 0L
    private lateinit var _coroutineScope: CoroutineScope

    private val _timerEvent = MutableSharedFlow<TimerEvent>()
    override val timerEvent = _timerEvent.asSharedFlow()

    override val millisInFuture: Long get() = _millisInFuture
    override val countDownInterval: Long get() = _countDownInterval


    override fun setCoroutineScope(coroutineScope: CoroutineScope)
    {
        _coroutineScope = coroutineScope
    }

    override fun start() = countDownTimer.start()
    override fun restart() = countDownTimer.restart()
    override fun stop() = countDownTimer.stop()
    override fun resume() = countDownTimer.resume()

    override fun initialize(millisInFuture: Long, countDownInterval: Long)
    {
        _millisInFuture = millisInFuture
        _countDownInterval = countDownInterval

        countDownTimer = object : PreciseCountDownTimer(millisInFuture, countDownInterval)
        {
            override fun onStart()
            {
                _coroutineScope.launch(Dispatchers.IO) { _timerEvent.emit(TimerEvent.OnStart) }
            }

            override fun onTick(millisUntilFinished: Long)
            {
                _coroutineScope.launch(Dispatchers.IO) { _timerEvent.emit(TimerEvent.OnTick(millisUntilFinished)) }
            }

            override fun onRestart()
            {
                _coroutineScope.launch(Dispatchers.IO) { _timerEvent.emit(TimerEvent.OnRestart) }
            }

            override fun onStop()
            {
                _coroutineScope.launch(Dispatchers.IO) { _timerEvent.emit(TimerEvent.OnStop) }
            }

            override fun onResume()
            {
                _coroutineScope.launch(Dispatchers.IO) { _timerEvent.emit(TimerEvent.OnResume) }
            }

            override fun onFinish()
            {
                _coroutineScope.launch(Dispatchers.IO) { _timerEvent.emit(TimerEvent.OnFinish) }
            }
        }
    }
}
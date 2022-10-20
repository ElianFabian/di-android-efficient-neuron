package com.elian.computeit.feature_tests.data

import com.elian.computeit.core.domain.util.count_down_timer.CountDownTimer
import com.elian.computeit.core.domain.util.count_down_timer.CountDownTimerEvent
import com.elian.computeit.core.util.PreciseCountDown
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PreciseCountDownImpl : CountDownTimer
{
    private lateinit var countDownTimer: PreciseCountDown
    private var _millisInFuture = 0L
    private var _countDownInterval = 0L
    private lateinit var _coroutineScope: CoroutineScope

    private val _timerEvent = MutableSharedFlow<CountDownTimerEvent>()
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

    override fun initialize(millisInFuture: Long, countDownInterval: Long)
    {
        _millisInFuture = millisInFuture
        _countDownInterval = countDownInterval

        countDownTimer = object : PreciseCountDown(millisInFuture, countDownInterval)
        {
            override fun onStart()
            {
                _coroutineScope.launch(Dispatchers.IO) { _timerEvent.emit(CountDownTimerEvent.Started) }
            }

            override fun onRestart()
            {
                _coroutineScope.launch(Dispatchers.IO) { _timerEvent.emit(CountDownTimerEvent.Restarted) }
            }

            override fun onStop()
            {
                _coroutineScope.launch(Dispatchers.IO) { _timerEvent.emit(CountDownTimerEvent.Stopped) }
            }

            override fun onTick(millisUntilFinished: Long)
            {
                _coroutineScope.launch(Dispatchers.IO) { _timerEvent.emit(CountDownTimerEvent.Ticked(millisUntilFinished)) }
            }

            override fun onFinish()
            {
                _coroutineScope.launch(Dispatchers.IO) { _timerEvent.emit(CountDownTimerEvent.Finished) }
            }
        }
    }
}
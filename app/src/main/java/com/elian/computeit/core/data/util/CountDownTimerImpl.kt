package com.elian.computeit.core.data.util

import com.elian.computeit.core.domain.util.CountDownTimer
import com.elian.computeit.core.domain.util.TimerEvent
import com.elian.computeit.core.util.PreciseCountDownTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CountDownTimerImpl : CountDownTimer
{
	private var countDownTimer: PreciseCountDownTimer? = null
	private var _millisInFuture = 0L
	private var _countDownInterval = 0L
	private lateinit var _coroutineScope: CoroutineScope

	private val _timerEventFlow = Channel<TimerEvent>()
	override val timerEventFlow = _timerEventFlow.receiveAsFlow()


	override fun start()
	{
		countDownTimer?.start()
	}

	override fun restart()
	{
		countDownTimer?.restart()
	}

	override fun stop()
	{
		countDownTimer?.stop()
	}

	override fun resume()
	{
		countDownTimer?.resume()
	}

	override fun initialize(
		millisInFuture: Long,
		countDownInterval: Long,
		coroutineScope: CoroutineScope,
	)
	{
		_millisInFuture = millisInFuture
		_countDownInterval = countDownInterval
		_coroutineScope = coroutineScope

		countDownTimer = object : PreciseCountDownTimer(millisInFuture, countDownInterval)
		{
			override fun onStart()
			{
				_coroutineScope.launch { _timerEventFlow.send(TimerEvent.OnStart) }
			}

			override fun onTick(millisUntilFinished: Long)
			{
				_coroutineScope.launch { _timerEventFlow.send(TimerEvent.OnTick(millisUntilFinished)) }
			}

			override fun onRestart()
			{
				_coroutineScope.launch { _timerEventFlow.send(TimerEvent.OnRestart) }
			}

			override fun onStop()
			{
				_coroutineScope.launch { _timerEventFlow.send(TimerEvent.OnStop) }
			}

			override fun onResume()
			{
				_coroutineScope.launch { _timerEventFlow.send(TimerEvent.OnResume) }
			}

			override fun onFinish()
			{
				_coroutineScope.launch { _timerEventFlow.send(TimerEvent.OnFinish) }
			}
		}
	}
}
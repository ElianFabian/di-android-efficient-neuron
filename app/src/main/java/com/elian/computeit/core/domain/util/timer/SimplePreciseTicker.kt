package com.elian.computeit.core.domain.util.timer

// Based on: https://stackoverflow.com/questions/23323823/android-countdowntimer-tick-is-not-accurate

// TODO: I noticed that when the app is open for a certain time (even doing nothing)
// sometimes when play() and resume() back and forth, when resuming some little but
// there's some noticeable delay before resuming.
// I don't know what causes that behavior.

abstract class SimplePreciseTicker @JvmOverloads constructor(
	periodInMillis: Long = 1,
) : java.util.Timer(true) {

	private var task = newTask()
	private var hasStarted = false
	private var isRunning = false

	var periodInMillis = periodInMillis
		set(period) {
			require(periodInMillis > 0) {
				"intervalInMillis $periodInMillis must be greater than 0."
			}

			field = period

			if (isRunning) {
				task.cancel()
				task = newTask()
				scheduleAtFixedRate(task, period, period)
			}
		}


	abstract fun onTick()


	fun play() {
		if (hasStarted) {
			task = newTask()
		}

		hasStarted = true
		isRunning = true
		scheduleAtFixedRate(task, periodInMillis, periodInMillis)
	}

	fun pause() {
		task.cancel()
		isRunning = false
	}

	fun dispose() {
		cancel()
		isRunning = false
		purge()
	}


	private fun newTask() = object : java.util.TimerTask() {
		override fun run() = onTick()
	}
}
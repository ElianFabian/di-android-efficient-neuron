package com.elian.computeit.core.util

import java.util.Timer
import java.util.TimerTask

// https://stackoverflow.com/questions/23323823/android-countdowntimer-tick-is-not-accurate
abstract class SimplePreciseTimer @JvmOverloads constructor(
	val intervalInMillis: Long = 1,
) : Timer(null, true) {

	private var task = getTask()
	private var startTimeInMillis = -1L
	private var wasStarted = false


	init {
		require(intervalInMillis > 0) {
			"intervalInMillis $intervalInMillis must be greater than 0."
		}
	}


	abstract fun onTick()


	fun play() {
		if (wasStarted) {
			resume()
		}

		start()
	}

	fun pause() {
		task.cancel()
	}

	fun dispose() {
		cancel()
		purge()
	}


	private fun start() {
		wasStarted = true
		scheduleAtFixedRate(task, 0, intervalInMillis)
	}

	private fun resume() {
		task = getTask()
		startTimeInMillis = -1
		play()
	}

	private fun getTask() = object : TimerTask() {
		override fun run() = onTick()
	}
}
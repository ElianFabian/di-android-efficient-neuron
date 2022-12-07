package com.elian.computeit.feature_tests.presentation.test

sealed interface TestEvent
{
	data class OnTimerTickInNormalMode(
		val millisSinceStart: Long,
		val millisUntilFinished: Long,
	) : TestEvent

	data class OnTimerTickInInfiniteMode(val millisSinceStart: Long) : TestEvent

	object OnTimerFinish : TestEvent
	data class OnGoToTestDetails(val args: List<Pair<String, Any>>) : TestEvent
}

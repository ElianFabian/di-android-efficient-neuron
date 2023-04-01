package com.elian.computeit.feature_tests.presentation.test

import com.elian.computeit.feature_tests.domain.args.TestDetailsArgs

sealed interface TestEvent
{
	data class OnTimerTickInNormalMode(
		val millisSinceStart: Long,
		val millisUntilFinished: Long,
	) : TestEvent

	data class OnTimerTickInInfiniteMode(val millisSinceStart: Long) : TestEvent

	object OnTimerFinish : TestEvent
	data class OnGoToTestDetails(val args: TestDetailsArgs) : TestEvent
}

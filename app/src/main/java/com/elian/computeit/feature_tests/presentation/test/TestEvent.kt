package com.elian.computeit.feature_tests.presentation.test

import com.elian.computeit.feature_tests.domain.args.TestDetailsArgs

sealed interface TestEvent {
	object OnTimerFinish : TestEvent
	data class OnGoToTestDetails(val args: TestDetailsArgs) : TestEvent
}

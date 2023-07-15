package com.elian.computeit.core.presentation

import com.elian.computeit.feature_tests.domain.args.TestDetailsArgs

sealed interface HomeEvent {
	class OnGoToTestDetail(val args: TestDetailsArgs) : HomeEvent
}
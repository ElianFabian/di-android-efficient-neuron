package com.elian.computeit.feature_tests.presentation.test

sealed interface TestEvent
{
    data class OnTimerTick(val millisUntilFinished: Long): TestEvent
    object OnTimerFinish : TestEvent
}

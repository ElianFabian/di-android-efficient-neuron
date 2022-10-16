package com.elian.computeit.feature_tests.presentation.test

sealed interface TestEvent
{
    data class TimerTicked(val millisUntilFinished: Long): TestEvent
    object TimerFinished : TestEvent
}

package com.elian.computeit.feature_tests.presentation.test

sealed interface TestAction
{
    data class EnteredNumber(val value: Int) : TestAction
    object ClearInput : TestAction
    object NextTest : TestAction
}
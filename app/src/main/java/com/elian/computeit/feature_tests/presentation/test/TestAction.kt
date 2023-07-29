package com.elian.computeit.feature_tests.presentation.test

sealed interface TestAction {
	object StartTest : TestAction
	data class EnterDigit(val digit: Int) : TestAction
	object RemoveLastDigit : TestAction
	object ClearInput : TestAction
	object NextOperation : TestAction
	object ForceFinish : TestAction
}
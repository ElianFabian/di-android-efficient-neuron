package com.elian.computeit.feature_tests.presentation.test

sealed interface TestAction
{
	data class EnterNumber(val value: Int) : TestAction
	object RemoveLastDigit : TestAction
	object ClearInput : TestAction
	object NextOperation : TestAction
	object ForceFinish : TestAction
}
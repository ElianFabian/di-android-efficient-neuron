package com.elian.computeit.feature_tests.presentation.test

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TestViewModel : ViewModel()
{
    private val _currentNumberState = MutableStateFlow(0)
    val currentNumberState = _currentNumberState.asStateFlow()


    fun onAction(action: TestAction) = when (action)
    {
        is TestAction.EnteredNumber ->
        {
            _currentNumberState.value = _currentNumberState.value * 10 + action.value
        }
        TestAction.ClearInput       ->
        {
            _currentNumberState.value = 0
        }
        TestAction.NextTest         ->
        {
            _currentNumberState.value = 0
        }
    }
}
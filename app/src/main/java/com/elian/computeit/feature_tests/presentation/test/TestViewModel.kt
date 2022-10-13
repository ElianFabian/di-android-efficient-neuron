package com.elian.computeit.feature_tests.presentation.test

import androidx.lifecycle.ViewModel
import com.elian.computeit.core.util.extensions.clampLength
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
            val newNumber = _currentNumberState.value * 10 + action.value
            _currentNumberState.value = newNumber.clampLength(maxLength = 8)
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
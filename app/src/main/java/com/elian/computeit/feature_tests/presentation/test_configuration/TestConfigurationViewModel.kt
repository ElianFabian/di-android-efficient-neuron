package com.elian.computeit.feature_tests.presentation.test_configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.computeit.core.util.EXTRA_TEST_COUNT
import com.elian.computeit.core.util.EXTRA_TEST_TIME_IN_SECONDS
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationAction.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestConfigurationViewModel @Inject constructor() : ViewModel()
{
    private val argsToSend = mutableSetOf<Pair<String, Any>>()

    private val _eventFlow = MutableSharedFlow<TestConfigurationEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun onAction(action: TestConfigurationAction)
    {
        when (action)
        {
            is EnterSeconds   -> argsToSend.add(EXTRA_TEST_TIME_IN_SECONDS to action.seconds)
            is EnterTestCount -> argsToSend.add(EXTRA_TEST_COUNT to action.testCount)
            is EnterRange     ->
            {
                // TODO: range
            }
            is Play           ->
            {
                viewModelScope.launch()
                {
                    _eventFlow.emit(TestConfigurationEvent.OnPlay(argsToSend))
                }
            }
        }
    }
}
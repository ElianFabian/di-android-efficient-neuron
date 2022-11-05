package com.elian.computeit.feature_tests.presentation.test_end

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.elian.computeit.core.util.EXTRA_TEST_SESSION_DATA
import com.elian.computeit.core.util.extensions.speedOverTimeInSeconds
import com.elian.computeit.feature_tests.data.models.TestSessionData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TestEndViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel()
{
    private val _testSessionData = savedStateHandle.get<TestSessionData>(EXTRA_TEST_SESSION_DATA)!!

    fun getSpeedOverTimeInSeconds() = _testSessionData.speedOverTimeInSeconds
    fun getAverageSpeed() = getSpeedOverTimeInSeconds().values.last()
    fun getTestTimeInSeconds() = _testSessionData.testTimeInSeconds
    fun getTestCount() = _testSessionData.testDataList.size
}
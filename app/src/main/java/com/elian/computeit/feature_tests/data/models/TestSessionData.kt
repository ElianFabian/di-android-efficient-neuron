package com.elian.computeit.feature_tests.data.models

import com.elian.computeit.core.util.extensions.fromMillisToSeconds

data class TestSessionData(
    val dateInSeconds: Long,
    val testTimeInMillis: Long,
    val testDataList: List<TestData>,
)
{
    fun getAverageVelocityInTestsPerSecond(): Float = testDataList.size / testTimeInMillis.fromMillisToSeconds()
}
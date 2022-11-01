package com.elian.computeit.feature_tests.data.models

data class TestSessionData(
    val dateInSeconds: Long,
    val testTimeInMillis: Long,
    val testDataList: List<TestData>,
    val range: Range,
)
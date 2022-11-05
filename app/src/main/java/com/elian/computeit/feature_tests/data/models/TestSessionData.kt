package com.elian.computeit.feature_tests.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestSessionData(
    val dateInSeconds: Long,
    val testTimeInSeconds: Int,
    val testDataList: List<TestData>,
    val range: Range,
) : Parcelable
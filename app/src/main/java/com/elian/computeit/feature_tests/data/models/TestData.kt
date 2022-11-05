package com.elian.computeit.feature_tests.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestData(
    val pairOfNumbers: Pair<Int, Int>,
    val operation: String,
    val insertedResult: Int,
    val expectedResult: Int,
    val millisSinceStart: Long,
) : Parcelable
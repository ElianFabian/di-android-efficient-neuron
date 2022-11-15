package com.elian.computeit.core.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestData(
    val pairOfNumbers: NumberPair = NumberPair(),
    val operation: String = "",
    val insertedResult: Int = 0,
    val expectedResult: Int = 0,
    val millisSinceStart: Long = 0,
) : Parcelable
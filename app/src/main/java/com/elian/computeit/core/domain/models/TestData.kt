package com.elian.computeit.core.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestData(
    val dateInSeconds: Long = 0,
    val testTimeInSeconds: Int = 0,
    val testDataList: List<OperationData> = emptyList(),
    val range: Range? = null,
) : Parcelable
package com.elian.computeit.feature_tests.domain.model

import android.os.Parcelable
import com.elian.computeit.core.domain.models.TestData
import com.elian.computeit.core.util.extensions.*
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestInfo(
    val opm: Int,
    val rawOpm: Int,
    val timeInSeconds: Int,
    val operationCount: Int,
    val errorCount: Int,
    val opmPerSecond: Map<Int, Int>,
    val rawOpmPerSecond: Map<Int, Int>,
) : Parcelable


fun TestData.toTestInfo() = run()
{
    TestInfo(
        opm = opm,
        rawOpm = rawOpm,
        timeInSeconds = testTimeInSeconds,
        operationCount = operationDataList.size,
        errorCount = errorCount,
        opmPerSecond = opmPerSecond,
        rawOpmPerSecond = rawOpmPerSecond,
    )
}
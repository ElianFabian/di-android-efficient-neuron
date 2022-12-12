package com.elian.computeit.core.domain.repository

import com.elian.computeit.core.domain.models.TestData
import com.elian.computeit.feature_tests.domain.model.SpeedHistogramInfo

interface TestDataRepository
{
	suspend fun addTestData(testData: TestData)
	suspend fun getListOfTestData(): List<TestData>
	fun getSpeedHistogramInfo(rangeLength: Int): SpeedHistogramInfo
}
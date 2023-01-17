package com.elian.computeit.core.domain.repository

import com.elian.computeit.core.data.model.TestData

interface TestDataRepository
{
	suspend fun addTestData(userUuid: String, testData: TestData)
	suspend fun getListOfTestData(userUuid: String): List<TestData>
	fun getTestsPerSpeedRange(rangeLength: Int): List<Int>
}
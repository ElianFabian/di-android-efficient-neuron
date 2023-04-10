package com.elian.computeit.core.domain.repository

import com.elian.computeit.core.data.model.TestData

interface TestDataRepository
{
	val isDataCached: Boolean

	suspend fun addTestData(userUuid: String, testData: TestData)
	suspend fun getListOfTestData(userUuid: String): List<TestData>
	fun getTestCountPerSpeedRange(rangeLength: Int): List<Int>
}
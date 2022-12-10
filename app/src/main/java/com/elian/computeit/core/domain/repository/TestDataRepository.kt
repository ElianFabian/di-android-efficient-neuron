package com.elian.computeit.core.domain.repository

import com.elian.computeit.core.domain.models.TestData

interface TestDataRepository
{
	suspend fun addTestData(testData: TestData)
	suspend fun getListOfTestData(): List<TestData>
}
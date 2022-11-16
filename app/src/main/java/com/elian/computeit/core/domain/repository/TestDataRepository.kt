package com.elian.computeit.core.domain.repository

import com.elian.computeit.core.domain.models.TestData
import kotlinx.coroutines.flow.Flow

interface TestDataRepository
{
    suspend fun addTestData(testData: TestData)
    suspend fun getTestDataList(): Flow<List<TestData>>
}
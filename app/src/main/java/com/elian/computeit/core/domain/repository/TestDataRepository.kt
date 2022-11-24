package com.elian.computeit.core.domain.repository

import com.elian.computeit.core.domain.models.TestData
import com.elian.computeit.feature_tests.domain.models.TestListInfo
import kotlinx.coroutines.flow.Flow

interface TestDataRepository
{
    suspend fun addTestData(testData: TestData)
    fun getTestListInfo(): Flow<TestListInfo>
}
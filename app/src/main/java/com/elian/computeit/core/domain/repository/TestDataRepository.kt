package com.elian.computeit.core.domain.repository

import com.elian.computeit.core.domain.models.TestSessionData
import kotlinx.coroutines.flow.Flow

interface TestDataRepository
{
    suspend fun addTestSessionData(testSessionData: TestSessionData)
    suspend fun getTestSessionDataList(): Flow<List<TestSessionData>>
}
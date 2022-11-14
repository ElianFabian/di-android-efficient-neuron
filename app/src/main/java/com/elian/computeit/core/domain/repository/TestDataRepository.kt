package com.elian.computeit.core.domain.repository

import com.elian.computeit.core.domain.models.TestSessionData

interface TestDataRepository
{
    suspend fun addTestSessionData(testSessionData: TestSessionData)
}
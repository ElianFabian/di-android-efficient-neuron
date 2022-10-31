package com.elian.computeit.feature_tests.domain.repository

import com.elian.computeit.feature_tests.data.models.TestSessionData

interface TestDataRepository
{
    suspend fun saveTestSessionData(testSessionData: TestSessionData)
}
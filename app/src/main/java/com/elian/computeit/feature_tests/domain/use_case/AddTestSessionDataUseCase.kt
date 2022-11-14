package com.elian.computeit.feature_tests.domain.use_case

import com.elian.computeit.core.domain.models.TestSessionData
import com.elian.computeit.core.domain.repository.TestDataRepository
import javax.inject.Inject

class AddTestSessionDataUseCase @Inject constructor(
    private val repository: TestDataRepository,
)
{
    suspend operator fun invoke(testSessionData: TestSessionData) = repository.addTestSessionData(testSessionData)
}
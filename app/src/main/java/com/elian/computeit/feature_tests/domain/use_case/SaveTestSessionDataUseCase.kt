package com.elian.computeit.feature_tests.domain.use_case

import com.elian.computeit.feature_tests.data.models.TestSessionData
import com.elian.computeit.feature_tests.domain.repository.TestDataRepository
import javax.inject.Inject

class SaveTestSessionDataUseCase @Inject constructor(private val repository: TestDataRepository)
{
    suspend operator fun invoke(testSessionData: TestSessionData) = repository.saveTestSessionData(testSessionData)
}
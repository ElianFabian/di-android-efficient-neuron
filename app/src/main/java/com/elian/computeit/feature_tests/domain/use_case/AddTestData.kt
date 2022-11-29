package com.elian.computeit.feature_tests.domain.use_case

import com.elian.computeit.core.domain.models.TestData
import com.elian.computeit.core.domain.repository.TestDataRepository
import javax.inject.Inject

class AddTestData @Inject constructor(
	private val repository: TestDataRepository,
)
{
	suspend operator fun invoke(testData: TestData) = repository.addTestData(testData)
}
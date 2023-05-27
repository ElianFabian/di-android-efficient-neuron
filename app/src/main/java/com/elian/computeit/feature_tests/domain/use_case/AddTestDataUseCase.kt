package com.elian.computeit.feature_tests.domain.use_case

import com.elian.computeit.core.data.model.TestData
import com.elian.computeit.core.domain.repository.TestDataRepository
import javax.inject.Inject

class AddTestDataUseCase @Inject constructor(
	private val repository: TestDataRepository,
) {
	suspend operator fun invoke(
		userUuid: String,
		testData: TestData,
	) {
		return repository.addTestData(
			userUuid = userUuid,
			testData = testData
		)
	}
}
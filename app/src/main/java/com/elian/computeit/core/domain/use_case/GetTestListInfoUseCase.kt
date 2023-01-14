package com.elian.computeit.core.domain.use_case

import com.elian.computeit.core.data.toTestListInfo
import com.elian.computeit.core.domain.repository.TestDataRepository
import com.elian.computeit.feature_tests.domain.model.TestListInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTestListInfoUseCase @Inject constructor(
	private val repository: TestDataRepository,
)
{
	suspend operator fun invoke(userUuid: String): TestListInfo
	{
		val listOfData = repository.getListOfTestData(userUuid)

		return withContext(Dispatchers.Default)
		{
			listOfData.toTestListInfo()
		}
	}
}
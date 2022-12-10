package com.elian.computeit.core.domain.use_case

import com.elian.computeit.core.data.toTestListInfo
import com.elian.computeit.core.domain.repository.TestDataRepository
import com.elian.computeit.feature_tests.domain.model.TestListInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTestListInfo @Inject constructor(
	private val repository: TestDataRepository,
)
{
	suspend operator fun invoke(): TestListInfo
	{
		val listOfData = repository.getListOfTestData()

		return withContext(Dispatchers.Default)
		{
			listOfData.toTestListInfo()
		}
	}
}
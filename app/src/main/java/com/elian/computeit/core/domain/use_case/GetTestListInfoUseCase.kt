package com.elian.computeit.core.domain.use_case

import com.elian.computeit.core.data.mapper.toTestListInfo
import com.elian.computeit.core.domain.models.TestListInfo
import com.elian.computeit.core.domain.repository.TestDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTestListInfoUseCase @Inject constructor(
	private val repository: TestDataRepository,
) {
	val isDataCached get() = repository.isDataCached


	suspend operator fun invoke(userUuid: String): TestListInfo {
		val listOfData = repository.getListOfTestData(userUuid)

		return withContext(Dispatchers.Default) {
			listOfData.toTestListInfo()
		}
	}
}
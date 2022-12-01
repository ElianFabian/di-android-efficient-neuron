package com.elian.computeit.core.domain.use_case

import com.elian.computeit.core.domain.repository.TestDataRepository
import javax.inject.Inject

class GetTestListInfo @Inject constructor(
	private val repository: TestDataRepository,
)
{
	suspend operator fun invoke() = repository.getTestListInfo()
}
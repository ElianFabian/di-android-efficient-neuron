package com.elian.computeit.core.domain.use_case

import com.elian.computeit.core.domain.repository.TestDataRepository
import javax.inject.Inject

class GetSpeedHistogramInfoUseCase @Inject constructor(
	private val repository: TestDataRepository,
)
{
	operator fun invoke(rangeLength: Int) = repository.getSpeedHistogramInfo(rangeLength)
}
package com.elian.computeit.core.domain.use_case

import com.elian.computeit.core.domain.repository.TestDataRepository
import javax.inject.Inject

class GetTestListInfoUseCase @Inject constructor(
    private val repository: TestDataRepository,
)
{
    operator fun invoke() = repository.getTestListInfo()
}
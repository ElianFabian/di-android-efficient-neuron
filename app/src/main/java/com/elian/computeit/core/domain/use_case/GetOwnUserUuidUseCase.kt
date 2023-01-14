package com.elian.computeit.core.domain.use_case

import com.elian.computeit.core.domain.repository.LocalAppDataRepository
import javax.inject.Inject

class GetOwnUserUuidUseCase @Inject constructor(
	private val appData: LocalAppDataRepository,
)
{
	suspend operator fun invoke(): String = appData.getUserUuid()!!
}
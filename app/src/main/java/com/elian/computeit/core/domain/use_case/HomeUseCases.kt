package com.elian.computeit.core.domain.use_case

import javax.inject.Inject

class HomeUseCases @Inject constructor(
	val getTestListInfo: GetTestListInfoUseCase,
	val getTestCountPerSpeedRange: GetListOfTestsPerSpeedRangeUseCase,
	val getOwnUserUuid: GetOwnUserUuidUseCase,
)
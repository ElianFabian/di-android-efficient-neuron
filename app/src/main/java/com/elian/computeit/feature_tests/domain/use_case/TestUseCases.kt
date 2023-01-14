package com.elian.computeit.feature_tests.domain.use_case

import com.elian.computeit.core.domain.use_case.GetOwnUserUuidUseCase
import javax.inject.Inject

class TestUseCases @Inject constructor(
	val addTestData: AddTestDataUseCase,
	val getRandomNumberPairFromOperation: GetRandomNumberPairFromOperationUseCase,
	val getOwnUserUuid: GetOwnUserUuidUseCase,
)
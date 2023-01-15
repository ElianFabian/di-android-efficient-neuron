package com.elian.computeit.core.presentation

import com.elian.computeit.feature_tests.domain.model.TestListInfo

data class HomeState(
	val info: TestListInfo? = null,
	val isLoading: Boolean = false,
)

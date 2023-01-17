package com.elian.computeit.core.presentation

import com.elian.computeit.core.domain.models.TestListInfo

data class HomeState(
	val info: TestListInfo? = null,
	val isLoading: Boolean = false,
)

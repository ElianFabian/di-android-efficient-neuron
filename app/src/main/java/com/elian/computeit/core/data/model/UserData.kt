package com.elian.computeit.core.data.model

import com.elian.computeit.core.domain.models.TestData

data class UserData(
	val listOfTestData: List<TestData>? = emptyList(),
)
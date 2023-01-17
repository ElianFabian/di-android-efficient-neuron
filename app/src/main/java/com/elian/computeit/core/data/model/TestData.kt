package com.elian.computeit.core.data.model

import com.elian.computeit.core.domain.models.Range

data class TestData(
	val dateUnix: Long = 0,
	val timeInSeconds: Int = 0,
	val listOfOperationData: List<OperationData> = emptyList(),
	val range: Range? = null,
)
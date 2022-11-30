package com.elian.computeit.core.domain.models

data class TestData(
	val dateUnix: Long = 0,
	val timeInSeconds: Int = 0,
	val listOfOperationData: List<OperationData> = emptyList(),
	val range: Range? = null,
)
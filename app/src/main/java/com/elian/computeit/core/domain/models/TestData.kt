package com.elian.computeit.core.domain.models

data class TestData(
    val dateInSeconds: Long = 0,
    val testTimeInSeconds: Int = 0,
    val operationDataList: List<OperationData> = emptyList(),
    val range: Range? = null,
)
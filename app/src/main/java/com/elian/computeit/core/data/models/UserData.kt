package com.elian.computeit.core.data.models

import com.elian.computeit.core.domain.models.TestData

data class UserData(
    val testDataList: List<TestData> = emptyList(),
)
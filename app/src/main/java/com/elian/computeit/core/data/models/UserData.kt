package com.elian.computeit.core.data.models

import com.elian.computeit.core.domain.models.TestSessionData

data class UserData(
    val testSessionDataList: List<TestSessionData> = emptyList(),
)
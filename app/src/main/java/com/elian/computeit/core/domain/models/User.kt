package com.elian.computeit.core.domain.models

import java.util.*

data class User(
    val email: String = "",
    val password: String = "",
    val uuid: String = UUID.randomUUID().toString(),
)
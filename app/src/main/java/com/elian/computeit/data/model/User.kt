package com.elian.computeit.data.model

import java.util.*

data class User(
    val email: String = "",
    val password: String = "",
    val uuid: String = UUID.randomUUID().toString(),
)
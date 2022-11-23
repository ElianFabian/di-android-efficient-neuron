package com.elian.computeit.core.domain.models

import java.util.*

data class User(
    val name: String = "",
    val password: String = "",
    val profilePicUrl: String? = null,
    val createdAtInSeconds: Long = System.currentTimeMillis() / 1000,
    val uuid: String = UUID.randomUUID().toString(),
)
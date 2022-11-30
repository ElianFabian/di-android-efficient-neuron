package com.elian.computeit.core.domain.models

import java.util.*

data class User(
	val name: String = "",
	val password: String = "",
	val biography: String = "",
	val profilePicUrl: String? = null,
	val createdAtUnix: Long = System.currentTimeMillis(),
	val uuid: String = UUID.randomUUID().toString(),
)
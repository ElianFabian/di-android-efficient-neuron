package com.elian.computeit.core.domain.repository

import com.elian.computeit.core.domain.models.User


interface UtilRepository
{
	suspend fun getUserByUuid(uuid: String): User?
	suspend fun getUserByName(name: String): User?
	suspend fun isUsernameTaken(currentName: String, newName: String): Boolean
	suspend fun isUsernameTaken(name: String): Boolean
}
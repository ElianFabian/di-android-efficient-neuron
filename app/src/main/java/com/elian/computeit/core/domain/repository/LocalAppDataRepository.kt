package com.elian.computeit.core.domain.repository

interface LocalAppDataRepository
{
    suspend fun getUserUuid(): String?
    suspend fun saveUserUuid(uuid: String)

    suspend fun getUserEmail(): String?
    suspend fun saveUserEmail(email: String)

    suspend fun getUserName(): String?
    suspend fun saveUserName(name: String)
}
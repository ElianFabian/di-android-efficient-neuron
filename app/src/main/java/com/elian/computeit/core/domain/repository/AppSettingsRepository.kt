package com.elian.computeit.core.domain.repository

interface AppSettingsRepository
{
    suspend fun getUserUuid(): String?
    suspend fun saveUserUuid(uuid: String)
    
    suspend fun getUserEmail(): String?
    suspend fun saveUserEmail(email: String)
}
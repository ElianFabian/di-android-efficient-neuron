package com.elian.computeit.core.domain.repository

interface AppSettingsRepository
{
    suspend fun getCurrentUserUuid(): String?
    suspend fun saveCurrentUserUuid(uuid: String)
}
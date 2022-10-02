package com.elian.computeit.core.domain.repository

interface AppSettingsRepository
{
    suspend fun getUserEmail(): String?
    suspend fun saveUserEmail(email: String)
}
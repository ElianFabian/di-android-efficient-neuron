package com.elian.computeit.feature_auth.domain.repository

import com.elian.computeit.core.util.SimpleResource


interface AuthRepository
{
    suspend fun login(username: String, password: String): SimpleResource
    suspend fun register(username: String, password: String): SimpleResource
}
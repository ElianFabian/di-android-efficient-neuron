package com.elian.computeit.feature_auth.domain.repository

import com.elian.computeit.core.util.SimpleResource


interface AuthRepository
{
    suspend fun login(email: String, password: String): SimpleResource
    suspend fun register(email: String, username: String, password: String): SimpleResource
}
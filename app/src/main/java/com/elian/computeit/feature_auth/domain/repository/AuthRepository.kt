package com.elian.computeit.feature_auth.domain.repository

import com.elian.computeit.core.util.SimpleResource
import com.elian.computeit.data.model.User


interface AuthRepository
{
    suspend fun login(email: String, password: String): SimpleResource
    suspend fun register(email: String, password: String): SimpleResource
}
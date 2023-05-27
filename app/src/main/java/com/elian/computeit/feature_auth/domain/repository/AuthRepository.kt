package com.elian.computeit.feature_auth.domain.repository

import com.elian.computeit.core.util.SimpleResource
import com.elian.computeit.feature_auth.domain.params.LoginParams
import com.elian.computeit.feature_auth.domain.params.RegisterParams

interface AuthRepository {
	suspend fun login(params: LoginParams): SimpleResource
	suspend fun register(params: RegisterParams): SimpleResource
}
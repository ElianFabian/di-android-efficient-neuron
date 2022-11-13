package com.elian.computeit.core.domain.util.extensions

import com.elian.computeit.core.domain.repository.LocalAppDataRepository

suspend fun LocalAppDataRepository.isUserLoggedIn() = getUserUuid() != null
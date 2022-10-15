package com.elian.computeit.feature_auth.domain.use_case

import com.elian.computeit.core.domain.repository.AppSettingsRepository
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(
    private val settings: AppSettingsRepository,
)
{
    suspend operator fun invoke() = settings.getUserEmail() != null
}
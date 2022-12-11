package com.elian.computeit.feature_profile.domain.use_case

import com.elian.computeit.feature_profile.domain.repository.ProfileRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
	private val repository: ProfileRepository,
)
{
	suspend operator fun invoke() = repository.logout()
}
package com.elian.computeit.feature_profile.domain.use_case

import com.elian.computeit.feature_profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfileInfoUseCase @Inject constructor(
    private val repository: ProfileRepository
)
{
    operator fun invoke() = repository.getProfileInfo()
}
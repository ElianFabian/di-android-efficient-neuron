package com.elian.computeit.feature_profile.domain.use_case

import com.elian.computeit.core.domain.use_case.GetOwnUserUuidUseCase
import javax.inject.Inject

class ProfileUseCases @Inject constructor(
	val getProfileInfo: GetProfileInfoUseCase,
	val updateProfile: UpdateProfileUseCase,
	val logout: LogoutUseCase,
	val getOwnUserUuid: GetOwnUserUuidUseCase,
)
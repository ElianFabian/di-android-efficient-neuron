package com.elian.computeit.feature_profile.domain.use_case

import javax.inject.Inject

class ProfileUseCases @Inject constructor(
	val getProfileInfo: GetProfileInfoUseCase,
	val updateProfile: UpdateProfileUseCase,
	val logout: LogoutUseCase,
)
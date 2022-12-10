package com.elian.computeit.feature_profile.domain.use_case

import javax.inject.Inject

class ProfileUseCases @Inject constructor(
	val getProfileInfo: GetProfileInfo,
	val updateProfile: UpdateProfile,
	val logout: Logout,
)
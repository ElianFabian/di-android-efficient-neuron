package com.elian.computeit.feature_profile.domain.use_case

import android.net.Uri
import com.elian.computeit.core.domain.util.checkIfError
import com.elian.computeit.core.domain.util.validateName
import com.elian.computeit.feature_profile.domain.model.EditProfileResult
import com.elian.computeit.feature_profile.domain.repository.ProfileRepository
import javax.inject.Inject

class ValidateProfile @Inject constructor(
	private val repository: ProfileRepository,
)
{
	suspend operator fun invoke(
		username: String,
		biography: String,
		profilePicUri: Uri?,
	): EditProfileResult
	{
		val usernameError = validateName(username)

		return if (checkIfError(usernameError))
		{
			EditProfileResult(
				usernameError = usernameError,
			)
		}
		else EditProfileResult(
			resource = repository.updateProfileInfo(
				username = username,
				biography = biography,
				profilePicUri = profilePicUri,
			)
		)
	}
}
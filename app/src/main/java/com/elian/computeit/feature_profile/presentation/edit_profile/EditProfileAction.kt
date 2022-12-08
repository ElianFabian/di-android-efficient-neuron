package com.elian.computeit.feature_profile.presentation.edit_profile

import android.net.Uri

sealed interface EditProfileAction
{
	data class EnterUsername(val value: String) : EditProfileAction
	data class EnterBiography(val value: String) : EditProfileAction
	data class EnterProfilePic(val value: Uri?) : EditProfileAction
	object Save : EditProfileAction
}
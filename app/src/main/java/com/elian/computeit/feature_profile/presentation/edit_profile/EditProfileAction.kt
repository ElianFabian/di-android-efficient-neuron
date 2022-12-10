package com.elian.computeit.feature_profile.presentation.edit_profile

sealed interface EditProfileAction
{
	data class EnterUsername(val value: String) : EditProfileAction
	data class EnterBiography(val value: String) : EditProfileAction
	data class EnterProfilePic(val value: List<Byte> = emptyList()) : EditProfileAction

	object Save : EditProfileAction
}
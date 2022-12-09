package com.elian.computeit.feature_profile.presentation

sealed interface ProfileAction
{
	data class EnterUsername(val value: String) : ProfileAction
	data class EnterBiography(val value: String) : ProfileAction
	data class EnterProfilePic(val value: List<Byte> = emptyList()) : ProfileAction

	object Save : ProfileAction
}
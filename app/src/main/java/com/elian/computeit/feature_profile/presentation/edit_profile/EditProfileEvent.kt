package com.elian.computeit.feature_profile.presentation.edit_profile

import com.elian.computeit.core.util.UiText

sealed interface EditProfileEvent
{
	object OnSave : EditProfileEvent
	data class OnShowErrorMessage(val error: UiText) : EditProfileEvent
}
package com.elian.computeit.feature_profile.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileInfo(
	val username: String,
	val biography: String,
	val profilePicBytes: List<Byte>,
	val createdAt: String,
) : Parcelable

package com.elian.computeit.feature_tips.data.models

import java.io.Serializable

data class Tip(
	val title: String,
	val example: String,
	val id: Int = 0,
) :
	Serializable
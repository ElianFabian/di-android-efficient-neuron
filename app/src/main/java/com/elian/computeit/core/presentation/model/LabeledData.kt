package com.elian.computeit.core.presentation.model

import androidx.annotation.StringRes

data class LabeledData(
	@StringRes
	val labelResId: Int,
	val value: Any,
)


infix fun @receiver:StringRes Int.labelOf(value: Any) = LabeledData(
	labelResId = this,
	value = value,
)
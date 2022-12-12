package com.elian.computeit.core.presentation.model

import androidx.annotation.StringRes

data class LabeledData(
	@StringRes
	val labelResId: Int,
	val value: Any,
)

infix fun Any.withLabel(@StringRes resId: Int) = LabeledData(
	labelResId = resId,
	value = this,
)

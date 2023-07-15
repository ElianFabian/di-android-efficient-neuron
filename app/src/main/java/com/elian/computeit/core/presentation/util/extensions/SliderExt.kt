package com.elian.computeit.core.presentation.util.extensions

import com.google.android.material.slider.Slider

inline fun Slider.onValueChanged(
	crossinline onChanged: (value: Float, isFromUser: Boolean) -> Unit
) {
	addOnChangeListener { _, value, isFromUser ->
		onChanged(value, isFromUser)
	}
}
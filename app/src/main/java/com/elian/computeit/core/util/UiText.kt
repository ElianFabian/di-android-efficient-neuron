package com.elian.computeit.core.util

import android.content.Context
import androidx.annotation.StringRes
import com.elian.computeit.R

sealed class UiText {
	data class DynamicString(val value: String) : UiText()
	class StringResource(
		@StringRes val resId: Int,
		vararg val args: Any?,
	) : UiText()

	companion object {
		val unknownError = StringResource(R.string.Error_Unknown)
	}
}

fun UiText?.asString(context: Context?): String? = when (this) {
	is UiText.DynamicString  -> this.value
	is UiText.StringResource -> context?.getString(this.resId, *args)
	else                     -> null
}

fun UiText?.orUnknownError(): UiText = when {
	this == null -> UiText.unknownError
	else         -> this
}

fun UiText(
	@StringRes resId: Int,
	vararg args: Any?,
): UiText = UiText.StringResource(
	resId = resId,
	args = args,
)

fun UiText(value: String?): UiText = when (value) {
	null -> UiText.unknownError
	else -> UiText.DynamicString(value)
}
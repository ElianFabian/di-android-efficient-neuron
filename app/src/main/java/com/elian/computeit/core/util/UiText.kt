package com.elian.computeit.core.util

import android.content.Context
import androidx.annotation.StringRes
import com.elian.computeit.R

sealed interface UiText


private class DynamicString(val value: String) : UiText

private class StringResource(
	@StringRes val resId: Int,
	vararg val args: Any?,
) : UiText


fun UiText(
	@StringRes resId: Int,
	vararg args: Any?,
): UiText = StringResource(
	resId = resId,
	args = args,
)

fun UiText(value: String?): UiText = when (value) {
	null -> UnknownError
	else -> DynamicString(value)
}

fun UiText?.asString(context: Context?): String? = when (this) {
	is DynamicString  -> this.value
	is StringResource -> context?.getString(this.resId, *args)
	else              -> null
}


private val UnknownError = UiText(R.string.Error_Unknown)

fun UiText?.orUnknownError(): UiText = when {
	this == null -> UnknownError
	else         -> this
}

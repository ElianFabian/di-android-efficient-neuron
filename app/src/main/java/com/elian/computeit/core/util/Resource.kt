package com.elian.computeit.core.util

import androidx.annotation.StringRes

typealias SimpleResource = Resource<Unit>

sealed class Resource<T>(
	val data: T? = null,
	val uiText: UiText? = null,
)
{
	class Success<T>(data: T? = null) : Resource<T>(data)
	class Error<T> : Resource<T>
	{
		constructor(message: String?, data: T? = null) : super(data, if (message == null) null else UiText.DynamicString(message))
		constructor(
			@StringRes messageResId: Int,
			vararg args: Any?,
			data: T? = null,
		) : super(data, UiText.StringResource(messageResId, *args))
	}
}
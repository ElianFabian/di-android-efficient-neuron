package com.elian.computeit.core.presentation.util.extensions

import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.descendants

inline fun <reified T : View> ViewGroup.findViewsOfType(): Sequence<T> = sequence()
{
	for (child in descendants)
	{
		if (child !is T) continue

		yield(child)
	}
}

inline fun <reified T : View> ViewGroup.findViewsOfTypeWithTag(@StringRes tag: Int) = findViewsOfTypeWithTag<T>(context.getString(tag))

inline fun <reified T : View> ViewGroup.findViewsOfTypeWithTag(tag: String): Sequence<T> = sequence()
{
	for (child in descendants)
	{
		if (child !is T || child.tag != tag) continue

		yield(child)
	}
}
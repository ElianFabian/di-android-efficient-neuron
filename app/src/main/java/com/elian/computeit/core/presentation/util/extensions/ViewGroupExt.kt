package com.elian.computeit.core.presentation.util.extensions

import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.descendants

inline fun <reified T : View> ViewGroup.findViewsOfType(): List<T>
{
	val views = mutableListOf<T>()

	for (child in descendants)
	{
		if (child !is T) continue

		views.add(child)
	}

	return views
}

inline fun <reified T : View> ViewGroup.findViewsWithTagOfType(@StringRes tag: Int) = findViewsWithTagOfType<T>(context.getString(tag))

inline fun <reified T : View> ViewGroup.findViewsWithTagOfType(tag: String): List<T>
{
	val views = mutableListOf<T>()

	for (child in descendants)
	{
		if (child !is T || child.tag != tag) continue

		views.add(child)
	}

	return views
}
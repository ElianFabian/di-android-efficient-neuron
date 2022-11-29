package com.elian.computeit.core.presentation.util.extensions

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

@ColorInt
fun Context?.getColorCompat(@ColorRes id: Int) = ContextCompat.getColor(this!!, id)

@ColorInt
fun Context?.getThemeColor(@AttrRes id: Int): Int
{
	val typedValue = TypedValue()

	val typedArray = this!!.obtainStyledAttributes(typedValue.data, intArrayOf(id))
	val color = typedArray.getColor(0, 0)

	typedArray.recycle()

	return color
}   
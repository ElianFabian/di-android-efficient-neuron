package com.elian.computeit.core.presentation.util.extensions

import android.text.InputType
import android.widget.TextView
import androidx.core.view.isGone

var TextView.text2: CharSequence
	get() = text
	set(value)
	{
		text = value

		isGone = text.isNullOrBlank()
	}

fun TextView.setTextIfDistinct(text: CharSequence)
{
	if (this.text.toString() != text.toString()) this.text = text
}

var TextView.textSizeInSp: Float
	get() = textSize / resources.displayMetrics.density
	set(value)
	{
		textSize = value / resources.displayMetrics.density
	}

/**
 * Only works if android:inputType is not already set.
 */
fun TextView.allowMultilineAndDisableEnterNewLine()
{
	setRawInputType(InputType.TYPE_CLASS_TEXT)
	setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE)
}
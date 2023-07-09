package com.elian.computeit.core.presentation.util.extensions

import android.text.InputType
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.widget.doAfterTextChanged

var TextView.text2: CharSequence
	get() = text
	set(value) {
		text = value

		isGone = text.isNullOrBlank()
	}

fun TextView.setTextIfDistinct(text: CharSequence?) {

	val textNonNull = text ?: ""

	if (this.text.toString() != textNonNull) this.text = textNonNull
}

inline fun TextView.onTextChanged(
	crossinline action: (text: String) -> Unit
) = doAfterTextChanged {
	val textNonNull = text ?: ""
	action(textNonNull.toString())
}

var TextView.textSizeInSp: Float
	get() = textSize / resources.displayMetrics.density
	set(value) {
		textSize = value / resources.displayMetrics.density
	}

/**
 * Only works if android:inputType is not already set.
 */
fun TextView.allowMultilineAndDisableEnterNewLine() {
	setRawInputType(InputType.TYPE_CLASS_TEXT)
	setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE)
}
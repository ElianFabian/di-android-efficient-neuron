package com.elian.computeit.core.presentation.util.extensions

import com.google.android.material.textfield.TextInputLayout

/**
 * Allows the layout to adjust the available space when removing the error.
 */
var TextInputLayout.error2: CharSequence?
	get() = this.error
	set(value)
	{
		this.error = value
		if (value == null) this.isErrorEnabled = false
	}
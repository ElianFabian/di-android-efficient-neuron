package com.elian.computeit.core.presentation.util

import android.content.Context
import com.elian.computeit.R
import com.elian.computeit.core.domain.errors.TextFieldError
import com.elian.computeit.core.util.Error

fun getUsernameErrorMessage(context: Context?, error: Error?) = when (error)
{
	is TextFieldError.Empty    -> context!!.getString(R.string.error_cant_be_empty)
	is TextFieldError.Invalid  -> context!!.getString(R.string.error_username_invalid).format(error.validCharacters)
	is TextFieldError.TooShort -> context!!.getString(R.string.error_too_short).format(error.minLength)
	is TextFieldError.TooLong  -> context!!.getString(R.string.error_too_long).format(error.maxLength)
	else                       -> null
}
package com.elian.computeit.core.presentation.util

import android.content.Context
import com.elian.computeit.R
import com.elian.computeit.core.domain.errors.TextFieldError
import com.elian.computeit.core.util.Error

fun getUsernameErrorMessage(context: Context?, error: Error?) = when (error) {
	is TextFieldError.Empty    -> context!!.getString(R.string.Error_CantBeEmpty)
	is TextFieldError.Invalid  -> context!!.getString(R.string.Error_UsernameInvalid).format(error.validCharacters)
	is TextFieldError.TooShort -> context!!.getString(R.string.Error_TooShort).format(error.minLength)
	is TextFieldError.TooLong  -> context!!.getString(R.string.Error_TooLong).format(error.maxLength)
	else                       -> null
}
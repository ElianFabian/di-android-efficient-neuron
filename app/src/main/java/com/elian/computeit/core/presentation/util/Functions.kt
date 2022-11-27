package com.elian.computeit.core.presentation.util

import android.content.Context
import android.hardware.display.DisplayManager
import android.view.Display
import com.elian.computeit.R
import com.elian.computeit.core.domain.states.TextFieldError
import com.elian.computeit.core.util.Error


/**
 * Is the screen of the device on.
 * @param context the context
 * @return true when (at least one) screen is on
 */
fun isScreenOn(context: Context?): Boolean
{
    val dm = context!!.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    var screenOn = false

    for (display in dm.displays)
    {
        if (display.state != Display.STATE_OFF)
        {
            screenOn = true
        }
    }

    return screenOn
}

fun getUsernameErrorMessage(context: Context?, error: Error?) = when (error)
{
    is TextFieldError.Empty    -> context!!.getString(R.string.error_cant_be_empty)
    is TextFieldError.Invalid  -> context!!.getString(R.string.error_username_invalid).format(error.validCharacters)
    is TextFieldError.TooShort -> context!!.getString(R.string.error_too_short).format(error.minLength)
    is TextFieldError.TooLong  -> context!!.getString(R.string.error_too_long).format(error.maxLength)
    else                       -> null
}
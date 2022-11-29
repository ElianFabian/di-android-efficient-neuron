package com.elian.computeit.core.presentation.util

import android.content.Context
import android.hardware.display.DisplayManager
import android.view.Display

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
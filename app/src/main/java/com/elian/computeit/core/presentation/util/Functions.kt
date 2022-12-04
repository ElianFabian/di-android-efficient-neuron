package com.elian.computeit.core.presentation.util

import android.content.Context
import android.hardware.display.DisplayManager
import android.view.Display
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

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

fun Fragment.showAlertDialog(
	@StringRes messageResId: Int,
	@StringRes positiveTextResId: Int = android.R.string.ok,
	@StringRes negativeTextResId: Int = android.R.string.cancel,
	onPositiveClick: (() -> Unit)? = null,
	onNegativeClick: (() -> Unit)? = null,
)
{
	showAlertDialog(
		message = getString(messageResId),
		positiveText = getString(positiveTextResId),
		negativeText = getString(negativeTextResId),
		onPositiveClick = onPositiveClick,
		onNegativeClick = onNegativeClick,
	)
}

fun Fragment.showAlertDialog(
	message: String,
	positiveText: String,
	negativeText: String,
	onPositiveClick: (() -> Unit)? = null,
	onNegativeClick: (() -> Unit)? = null,
)
{
	AlertDialog.Builder(requireContext())
		.setMessage(message)
		.setPositiveButton(positiveText) { _, _ -> onPositiveClick?.invoke() }
		.setNegativeButton(negativeText) { _, _ -> onNegativeClick?.invoke() }
		.create()
		.show()
}
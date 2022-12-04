package com.elian.computeit.core.presentation.util

import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

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
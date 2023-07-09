package com.elian.computeit.core.presentation.util.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

fun Fragment.navigate(@IdRes action: Int, args: Bundle? = null) = findNavController().navigate(action, args)
fun Fragment.navigateUp() = findNavController().navigateUp()


inline fun <reified T : Activity> Fragment.navigateTo(
	args: Bundle = Bundle(),
	finish: Boolean = true,
) {
	startActivity(Intent(context, T::class.java).putExtras(args))
	if (finish) activity?.finish()
}

fun Fragment.showToast(text: CharSequence?) {
	Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(@StringRes resId: Int) {
	Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
}

fun Fragment.showSnackBar(
	message: CharSequence?,
	actionName: String,
	action: (view: View) -> Unit,
) {
	Snackbar.make(requireView(), message ?: "", Snackbar.LENGTH_SHORT).apply {
		setAction(actionName, action)
		show()
	}
}


@ColorInt
fun Fragment.getColorCompat(@ColorRes id: Int) = context.getColorCompat(id)

@ColorInt
fun Fragment.getThemeColor(@AttrRes id: Int) = context.getThemeColor(id)

fun Fragment.showAlertDialog(
	@StringRes messageResId: Int,
	@StringRes positiveTextResId: Int = android.R.string.ok,
	@StringRes negativeTextResId: Int = android.R.string.cancel,
	onPositiveClick: (() -> Unit)? = null,
	onNegativeClick: (() -> Unit)? = null,
) {
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
) {
	AlertDialog.Builder(requireContext())
		.setMessage(message)
		.setPositiveButton(positiveText) { _, _ -> onPositiveClick?.invoke() }
		.setNegativeButton(negativeText) { _, _ -> onNegativeClick?.invoke() }
		.create()
		.show()
}
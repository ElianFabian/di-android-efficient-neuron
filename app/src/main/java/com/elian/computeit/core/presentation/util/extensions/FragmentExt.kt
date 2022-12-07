package com.elian.computeit.core.presentation.util.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.navigate(@IdRes action: Int, args: Bundle? = null) = findNavController().navigate(action, args)
fun Fragment.navigateUp() = findNavController().navigateUp()

/**
 * You may use it in case you have an exception like this:
 *
 * java.lang.IllegalArgumentException: Navigation action/destination id/action_fromFragment_to_toFragment cannot be found from the current destination Destination(id/toFragment)
 */
fun Fragment.navigateSafe(
	@IdRes action: Int,
	@IdRes currentDestination: Int,
	args: Bundle? = null,
)
{
	if (findNavController().currentDestination?.id == currentDestination) navigate(action, args)
}

inline fun <reified T : Activity> Fragment.navigateTo(
	args: Bundle = Bundle(),
	finish: Boolean = true,
)
{
	startActivity(Intent(context, T::class.java).putExtras(args))
	if (finish) activity?.finish()
}

fun Fragment.showToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT)
{
	Toast.makeText(context, text, duration).show()
}

fun Fragment.showToast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT)
{
	Toast.makeText(context, resId, duration).show()
}

@ColorInt
fun Fragment.getColorCompat(@ColorRes id: Int) = context.getColorCompat(id)

@ColorInt
fun Fragment.getThemeColor(@AttrRes id: Int) = context.getThemeColor(id)
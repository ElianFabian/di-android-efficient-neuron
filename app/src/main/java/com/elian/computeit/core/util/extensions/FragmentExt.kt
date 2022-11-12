package com.elian.computeit.core.util.extensions

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.navigate(@IdRes action: Int, args: Bundle? = null) = findNavController().navigate(action, args)

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

fun Fragment.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT)
{
    Toast.makeText(context, text, duration).show()
}

fun Fragment.getColor2(@ColorRes id: Int) = ContextCompat.getColor(requireContext(), id)

fun Fragment.onBackPressed(action: (() -> Unit)? = null) = activity?.onBackPressedDispatcher?.addCallback(
    object : OnBackPressedCallback(true)
    {
        override fun handleOnBackPressed()
        {
            action?.invoke()
        }
    }
)

fun Fragment.disableNavigateUp() = onBackPressed()


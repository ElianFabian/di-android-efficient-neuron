package com.elian.computeit.core.util.extensions

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.navigate(@IdRes action: Int, args: Bundle? = null) = findNavController().navigate(action, args)

fun Fragment.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT)
{
    Toast.makeText(context, text, duration).show()
}

fun Fragment.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT)
{
    Toast.makeText(context, resId, duration).show()
}

fun Fragment.getColorRes(@ColorRes id: Int) = ContextCompat.getColor(requireContext(), id)

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


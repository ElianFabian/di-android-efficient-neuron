package com.elian.computeit.core.presentation.util.extensions

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.navigate(@IdRes action: Int, args: Bundle? = null) = findNavController().navigate(action, args)
fun Fragment.navigateUp() = findNavController().navigateUp()

fun Fragment.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT)
{
    Toast.makeText(context, text, duration).show()
}

@ColorInt
fun Fragment.getColorCompat(@ColorRes id: Int) = context.getColorCompat(id)

@ColorInt
fun Fragment.getThemeColor(@AttrRes id: Int) = context.getThemeColor(id)
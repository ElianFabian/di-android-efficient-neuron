package com.elian.efficientneuron.util.extension

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment


fun Fragment.navigate(@IdRes action: Int)
{
    NavHostFragment.findNavController(this).navigate(action)
}

fun Fragment.navigate(@IdRes action: Int, args: Bundle?)
{
    NavHostFragment.findNavController(this).navigate(action, args)
}
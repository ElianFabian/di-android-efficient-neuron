package com.elian.efficientneuron.extension

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment


fun Fragment.navigate(action: Int)
{
    NavHostFragment.findNavController(this).navigate(action)
}

fun Fragment.navigate(action: Int, args: Bundle?)
{
    NavHostFragment.findNavController(this).navigate(action, args)
}
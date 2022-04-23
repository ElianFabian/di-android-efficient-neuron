package com.elian.efficientneuron.extension

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

class FragmentExtension
{
    fun Fragment.navigate(action: Int)
    {
        NavHostFragment.findNavController(this).navigate(action)
    }

    fun Fragment.navigate(action: Int, args: Bundle?)
    {
        navigate(action, args)
    }
}
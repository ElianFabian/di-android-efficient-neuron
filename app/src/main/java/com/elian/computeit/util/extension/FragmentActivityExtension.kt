package com.elian.computeit.util.extension

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.elian.computeit.R


fun FragmentActivity.goToFragment(fragment: Fragment, args: Bundle? = null): Fragment
{
    fragment.arguments = args

    supportFragmentManager
        .beginTransaction()
        .replace(R.id.nav_host_fragment, fragment)
        .commit()

    return fragment
}
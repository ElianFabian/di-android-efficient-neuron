package com.elian.computeit.core.presentation.util.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.elian.computeit.R


fun FragmentActivity.goToFragment(fragment: Fragment, args: Bundle? = null): Fragment
{
	fragment.arguments = args

	supportFragmentManager
		.beginTransaction()
		.replace(R.id.navHostFragment, fragment)
		.commit()

	return fragment
}
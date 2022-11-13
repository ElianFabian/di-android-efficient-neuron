package com.elian.computeit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceFragmentCompat
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.NavigationDrawerFragmentTag

class SettingsFragment : PreferenceFragmentCompat(), NavigationDrawerFragmentTag
{
    // This is to make sure that the settings fragment is not transparent
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view.setBackgroundColor(resources.getColor(R.color.blue_200))

        return view
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?)
    {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}
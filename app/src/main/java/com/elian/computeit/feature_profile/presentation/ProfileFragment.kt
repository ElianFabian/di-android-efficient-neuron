package com.elian.computeit.feature_profile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elian.computeit.core.presentation.util.NavigationDrawerFragmentTag
import com.elian.computeit.databinding.FragmentProfileBinding


class ProfileFragment : Fragment(), NavigationDrawerFragmentTag
{
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }
}
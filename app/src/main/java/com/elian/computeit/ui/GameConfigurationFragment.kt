package com.elian.computeit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elian.computeit.R
import com.elian.computeit.databinding.FragmentGameConfigurationBinding
import com.elian.computeit.core.util.extensions.navigate

class GameConfigurationFragment : Fragment()
{
    private lateinit var binding: FragmentGameConfigurationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentGameConfigurationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    //region Methods

    private fun initUI()
    {
        binding.btnPlay.setOnClickListener { navigate(R.id.action_gameConfigFragment_to_gameFragment) }

        binding.tvCornerIcon.text = arguments?.getString("operation")
    }

    //endregion
}
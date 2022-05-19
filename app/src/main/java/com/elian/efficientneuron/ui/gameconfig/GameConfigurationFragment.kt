package com.elian.efficientneuron.ui.gameconfig

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elian.efficientneuron.R
import com.elian.efficientneuron.databinding.FragmentGameConfigurationBinding
import com.elian.efficientneuron.util.extension.navigate

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
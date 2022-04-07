package com.elian.efficientneuron.ui.gameconfig

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.elian.efficientneuron.R
import com.elian.efficientneuron.databinding.FragmentGameConfigurationBinding

class GameConfigurationFragment : Fragment(), View.OnClickListener
{
    private lateinit var binding: FragmentGameConfigurationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        // Inflate the layout for this fragment
        binding = FragmentGameConfigurationBinding.inflate(inflater, container, false)

        binding.btnPlay.setOnClickListener(this)
        
        return binding.root
    }

    override fun onClick(v: View?)
    {
        when (v?.id)
        {
            R.id.btnPlay -> showGame()
        }
    }

    private fun showGame() = NavHostFragment.findNavController(this)
        .navigate(R.id.action_gameConfigFragment_to_gameFragment)
}
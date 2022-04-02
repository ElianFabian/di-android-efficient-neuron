package com.elian.efficientneuron.ui.gameconfig

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.elian.efficientneuron.R
import com.elian.efficientneuron.databinding.FragmentGameConfigBinding

class GameConfigurationFragment : Fragment(), View.OnClickListener
{
    lateinit var binding: FragmentGameConfigBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        binding = FragmentGameConfigBinding.inflate(inflater, container, false)

        binding.btnPlay.setOnClickListener(this)

        //fillSpinner()

        return binding.root
    }

    // TODO: Implementar rellenar Spinner
    private fun fillSpinner()
    {
        val modes = resources.getStringArray(R.array.spr_modes)
        val adapter = null
        binding.spinner.adapter = adapter
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
package com.elian.efficientneuron.ui.endgame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elian.efficientneuron.databinding.FragmentEndGameBinding


class EndGameFragment : Fragment()
{
    private lateinit var binding: FragmentEndGameBinding
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View
    {
        binding = FragmentEndGameBinding.inflate(inflater)
        return binding.root
    }
}
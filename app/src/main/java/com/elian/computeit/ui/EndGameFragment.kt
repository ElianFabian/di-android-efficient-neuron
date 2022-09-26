package com.elian.computeit.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elian.computeit.databinding.FragmentEndGameBinding


class EndGameFragment : Fragment()
{
    private lateinit var binding: FragmentEndGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentEndGameBinding.inflate(inflater)
        return binding.root
    }
}
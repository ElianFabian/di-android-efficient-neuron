package com.elian.efficientneuron.ui.tip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elian.efficientneuron.databinding.FragmentTipBinding

class TipFragment : Fragment()
{
    private lateinit var binding: FragmentTipBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentTipBinding.inflate(inflater)
        return binding.root
    }
}
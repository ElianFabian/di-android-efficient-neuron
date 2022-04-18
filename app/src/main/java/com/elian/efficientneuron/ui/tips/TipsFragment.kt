package com.elian.efficientneuron.ui.tips

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elian.efficientneuron.databinding.FragmentTipsBinding

class TipsFragment : Fragment()
{
    private lateinit var binding: FragmentTipsBinding
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View
    {
        binding = FragmentTipsBinding.inflate(inflater)
        return binding.root
    }
}
package com.elian.computeit.feature_tips.presentation.tip_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elian.computeit.databinding.FragmentTipDetailsBinding

class TipDetailsFragment : Fragment()
{
    private lateinit var binding: FragmentTipDetailsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentTipDetailsBinding.inflate(inflater)
        return binding.root
    }
}
package com.elian.computeit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elian.computeit.databinding.FragmentStatisticsBinding


class StatisticsFragment : Fragment()
{
    private lateinit var binding: FragmentStatisticsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentStatisticsBinding.inflate(inflater)
        return binding.root
    }
}
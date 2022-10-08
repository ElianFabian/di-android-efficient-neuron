package com.elian.computeit.feature_tests.presentation.test_end

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elian.computeit.databinding.FragmentTestEndBinding


class TestEndFragment : Fragment()
{
    private lateinit var binding: FragmentTestEndBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentTestEndBinding.inflate(inflater)
        return binding.root
    }
}
package com.elian.efficientneuron.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elian.efficientneuron.R
import com.elian.efficientneuron.databinding.FragmentProfileBinding


class ProfileFragment : Fragment()
{
    private lateinit var binding: FragmentProfileBinding
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View
    {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }
}
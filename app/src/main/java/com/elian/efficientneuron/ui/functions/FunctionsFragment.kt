package com.elian.efficientneuron.ui.functions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.elian.efficientneuron.R
import com.elian.efficientneuron.databinding.FragmentFunctionsBinding


class FunctionsFragment : Fragment(), View.OnClickListener
{
    private lateinit var binding: FragmentFunctionsBinding

    //region Fragment Methods

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentFunctionsBinding.inflate(inflater, container, false)

        binding.glButtons.children.iterator().forEach { it.setOnClickListener(this) }

        return binding.root
    }

    override fun onClick(v: View?) = when (v?.id)
    {
        R.id.ibAddition       -> navigateTo(R.id.action_functionsFragment_to_gameConfigurationFragment)
        R.id.ibSubtraction    -> navigateTo(R.id.action_functionsFragment_to_gameConfigurationFragment)
        R.id.ibMultiplication -> navigateTo(R.id.action_functionsFragment_to_gameConfigurationFragment)
        R.id.ibDivision       -> navigateTo(R.id.action_functionsFragment_to_gameConfigurationFragment)

        else                  -> Unit
    }

    //endregion

    //region Methods

    private fun navigateTo(action: Int)
    {
        NavHostFragment.findNavController(this).navigate(action)
    }

    //endregion
}
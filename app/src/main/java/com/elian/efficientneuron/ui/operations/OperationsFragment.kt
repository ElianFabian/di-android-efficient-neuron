package com.elian.efficientneuron.ui.operations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.elian.efficientneuron.R
import com.elian.efficientneuron.databinding.FragmentOperationsBinding
import com.elian.efficientneuron.ui.gameconfig.GameConfigurationFragment


class OperationsFragment : Fragment(), View.OnClickListener
{
    private lateinit var binding: FragmentOperationsBinding

    //region Fragment Methods

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentOperationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    override fun onClick(v: View?) = when (v?.id)
    {
        R.id.ibAddition       -> navigateTo(R.id.action_functionsFragment_to_gameConfigurationFragment, binding.ibAddition.tag.toString())
        R.id.ibSubtraction    -> navigateTo(R.id.action_functionsFragment_to_gameConfigurationFragment, binding.ibSubtraction.tag.toString())
        R.id.ibMultiplication -> navigateTo(R.id.action_functionsFragment_to_gameConfigurationFragment, binding.ibMultiplication.tag.toString())
        R.id.ibDivision       -> navigateTo(R.id.action_functionsFragment_to_gameConfigurationFragment, binding.ibDivision.tag.toString())

        else                  -> Unit
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        binding.grdlButtons.children.iterator().forEach { it.setOnClickListener(this) }
    }

    private fun navigateTo(action: Int, operation: String)
    {
        NavHostFragment.findNavController(this).navigate(action, Bundle().apply()
        {
            putString("operation", operation)
        })
    }

    //endregion
}
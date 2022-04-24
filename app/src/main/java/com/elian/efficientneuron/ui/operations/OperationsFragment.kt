package com.elian.efficientneuron.ui.operations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.elian.efficientneuron.R
import com.elian.efficientneuron.databinding.FragmentOperationsBinding
import com.elian.efficientneuron.extension.navigate


class OperationsFragment : Fragment()
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

    //endregion

    //region Methods

    private fun initUI() = binding.grdlButtons.children.iterator().forEach()
    {
        navigateTo(R.id.action_functionsFragment_to_gameConfigurationFragment, it.tag.toString())
    }


    private fun navigateTo(action: Int, operation: String)
    {
        navigate(action, Bundle().apply()
        {
            putString("operation", operation)
        })
    }

    //endregion
}
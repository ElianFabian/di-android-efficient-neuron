package com.elian.computeit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.elian.computeit.R
import com.elian.computeit.core.util.EXTRA_OPERATION_SYMBOL
import com.elian.computeit.databinding.FragmentOperationsBinding
import com.elian.computeit.core.util.extensions.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OperationsFragment : Fragment()
{
    private lateinit var binding: FragmentOperationsBinding

    //region Fragment Methods

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentOperationsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initUi()
    }

    //endregion

    //region Methods

    private fun initUi() = binding.grdlButtons.children.forEach { button ->
        button.setOnClickListener()
        {
            navigate(R.id.action_operationsFragment_to_testConfigurationFragment, bundleOf(
                EXTRA_OPERATION_SYMBOL to it.tag as String
            ))
        }
    }

    //endregion
}
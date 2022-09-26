package com.elian.computeit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.elian.computeit.R
import com.elian.computeit.databinding.FragmentOperationsBinding
import com.elian.computeit.util.extension.navigate
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

        initUI()
    }

    //endregion

    //region Methods

    private fun initUI() = binding.grdlButtons.children.iterator().forEach()
    {
        it.setOnClickListener()
        {
            navigate(R.id.action_functionsFragment_to_gameConfigurationFragment, Bundle().apply()
            {
                putString("operation", it.tag.toString())
            })
        }
    }

    //endregion
}
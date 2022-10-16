package com.elian.computeit.feature_tests.presentation.test_configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.elian.computeit.R
import com.elian.computeit.core.util.EXTRA_OPERATION_SYMBOL
import com.elian.computeit.core.util.EXTRA_OPERATION_TYPE
import com.elian.computeit.core.util.extensions.navigate
import com.elian.computeit.databinding.FragmentTestConfigurationBinding
import com.elian.computeit.feature_tests.domain.models.Sum

class TestConfigurationFragment : Fragment()
{
    private lateinit var binding: FragmentTestConfigurationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentTestConfigurationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    //region Methods

    private fun initUI()
    {
        binding.btnPlay.setOnClickListener()
        {
            // TODO: finish this fragment
            navigate(
                R.id.action_testConfigurationFragment_to_testFragment,
                bundleOf(
                    EXTRA_OPERATION_TYPE to Sum
                )
            )
        }

        binding.tvCornerIcon.text = arguments?.getString(EXTRA_OPERATION_SYMBOL)
    }

    //endregion
}
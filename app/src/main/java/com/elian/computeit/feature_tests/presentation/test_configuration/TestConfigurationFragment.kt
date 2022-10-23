package com.elian.computeit.feature_tests.presentation.test_configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elian.computeit.R
import com.elian.computeit.core.util.EXTRA_TEST_COUNT
import com.elian.computeit.core.util.EXTRA_TEST_TIME_IN_SECONDS
import com.elian.computeit.core.util.extensions.collectLatestFlowWhenStarted
import com.elian.computeit.core.util.extensions.navigate
import com.elian.computeit.databinding.FragmentTestConfigurationBinding

class TestConfigurationFragment : Fragment()
{
    private val viewModel by viewModels<TestConfigurationViewModel>()
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
        subscribeToEvents()
    }

    //region Methods

    private fun initUI()
    {
        binding.apply()
        {
            btnPlay.setOnClickListener()
            {
                val extraTimeOrTestCount = when (spnModes.selectedItem as String)
                {
                    getString(R.string.array_test_modes_time)  -> EXTRA_TEST_TIME_IN_SECONDS
                    getString(R.string.array_test_modes_tests) -> EXTRA_TEST_COUNT

                    else                                       -> ""
                }

                val secondsOrTestCount = etTestsOrTime.text.toString().toInt()

                when (spnModes.selectedItem as String)
                {
                    getString(R.string.array_test_modes_time)  -> viewModel.onAction(TestConfigurationAction.EnterSeconds(secondsOrTestCount))
                    getString(R.string.array_test_modes_tests) -> viewModel.onAction(TestConfigurationAction.EnterTestCount(secondsOrTestCount))
                }

                viewModel.onAction(TestConfigurationAction.EnterRange(
                    min = tietMinValue.toString().toInt(),
                    max = tietMaxValue.toString().toInt()
                ))
            }
        }
    }

    private fun subscribeToEvents()
    {
        collectLatestFlowWhenStarted(viewModel.eventFlow)
        {
            when (it)
            {
                is TestConfigurationEvent.OnPlay ->
                {
                    navigate(
                        R.id.action_testConfigurationFragment_to_testFragment,
                        bundleOf(*it.args.toTypedArray())
                    )
                }
            }
        }
    }

    //endregion
}
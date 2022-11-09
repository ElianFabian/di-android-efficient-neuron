package com.elian.computeit.feature_tests.presentation.test_configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elian.computeit.R
import com.elian.computeit.core.util.Error
import com.elian.computeit.core.util.extensions.*
import com.elian.computeit.databinding.FragmentTestConfigurationBinding
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationAction.*
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationEvent.OnPlay
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationEvent.OnShowErrorMessage
import com.elian.computeit.feature_tests.presentation.util.ConfigurationError
import com.google.android.material.radiobutton.MaterialRadioButton

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


    private fun initUI()
    {
        binding.apply()
        {
            tietMinValue.onTextChangedClearError()
            tietMaxValue.onTextChangedClearError()
            etTime.onTextChangedClearError()

            val operationTypeList = rgOperationType.findViewsWithTagOfType<MaterialRadioButton>(R.string.tag_operation_type)

            operationTypeList.forEach { radioButton ->

                radioButton.setOnClickListener()
                {
                    viewModel.onAction(SelectOperationType(
                        symbol = radioButton.text.toString()
                    ))
                }
            }

            viewModel.onAction(SelectOperationType(
                symbol = operationTypeList.first().text.toString()
            ))

            btnStart.setOnClickListener()
            {
                val seconds = etTime.text.toString().toIntOrNull()

                // TODO: when the project is more advanced I will try to add support for both tests and time modes
//                when (spnModes.selectedItem as String)
//                {
//                    getString(R.string.array_test_modes_time)  -> viewModel.onAction(EnterSeconds(secondsOrTestCount))
//                    getString(R.string.array_test_modes_tests) -> viewModel.onAction(EnterTestCount(secondsOrTestCount))
//                }

                viewModel.onAction(EnterSeconds(seconds))

                viewModel.onAction(EnterRange(
                    min = tietMinValue.text.toString().toIntOrNull(),
                    max = tietMaxValue.text.toString().toIntOrNull()
                ))

                viewModel.onAction(Play)
            }
        }
    }

    private fun subscribeToEvents()
    {
        collectLatestFlowWhenStarted(viewModel.eventFlow)
        {
            when (it)
            {
                is OnPlay             ->
                {
                    navigate(
                        R.id.action_testConfigurationFragment_to_testFragment,
                        bundleOf(*it.args.toTypedArray())
                    )
                }
                is OnShowErrorMessage -> toast(it.error.asString(requireContext()))
            }
        }
        collectLatestFlowWhenStarted(viewModel.testConfigurationErrorState)
        {
            it?.forEach { error ->
                when (error)
                {
                    is ConfigurationError.RangeValuesAreInverted -> toast(R.string.error_range_values_are_inverted)
                }
            }
        }
        collectLatestFlowWhenStarted(viewModel.minValueState)
        {
            binding.tietMinValue.error = getFieldError(it.error)
        }
        collectLatestFlowWhenStarted(viewModel.maxValueState)
        {
            binding.tietMaxValue.error = getFieldError(it.error)
        }
        collectLatestFlowWhenStarted(viewModel.testCountOrTimeState)
        {
            binding.etTime.error = getFieldError(it.error)
        }
    }

    private fun getFieldError(error: Error?) = when (error)
    {
        is ConfigurationError.ValueEmpty -> getString(R.string.error_cant_be_empty)
        else                             -> null
    }
}
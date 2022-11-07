package com.elian.computeit.feature_tests.presentation.test_configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elian.computeit.R
import com.elian.computeit.core.util.extensions.collectLatestFlowWhenStarted
import com.elian.computeit.core.util.extensions.findViewsWithTagOfType
import com.elian.computeit.core.util.extensions.navigate
import com.elian.computeit.core.util.extensions.toast
import com.elian.computeit.databinding.FragmentTestConfigurationBinding
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

    //region Methods

    private fun initUI()
    {
        binding.apply()
        {
            tietMinValue.addTextChangedListener { setMinValueError(null) }
            tietMaxValue.addTextChangedListener { setMaxValueError(null) }
            etTime.addTextChangedListener { setTimeError(null) }

            val operationTypeList = rgOperationType.findViewsWithTagOfType<MaterialRadioButton>(R.string.tag_operation_type)

            operationTypeList.forEach { radioButton ->

                radioButton.setOnClickListener()
                {
                    viewModel.onAction(TestConfigurationAction.SelectOperationType(
                        symbol = radioButton.text.toString()
                    ))
                }
            }

            viewModel.onAction(TestConfigurationAction.SelectOperationType(
                symbol = operationTypeList.first().text.toString()
            ))

            btnStart.setOnClickListener()
            {
                val secondsOrTestCount = etTime.text.toString().toIntOrNull()

                // TODO: when the project is more advanced I will try to add support for both tests and time modes
//                when (spnModes.selectedItem as String)
//                {
//                    getString(R.string.array_test_modes_time)  -> viewModel.onAction(TestConfigurationAction.EnterSeconds(secondsOrTestCount))
//                    getString(R.string.array_test_modes_tests) -> viewModel.onAction(TestConfigurationAction.EnterTestCount(secondsOrTestCount))
//                }

                viewModel.onAction(TestConfigurationAction.EnterSeconds(secondsOrTestCount))

                viewModel.onAction(TestConfigurationAction.EnterRange(
                    min = tietMinValue.text.toString().toIntOrNull(),
                    max = tietMaxValue.text.toString().toIntOrNull()
                ))

                viewModel.onAction(TestConfigurationAction.Play)
            }
        }
    }

    private fun subscribeToEvents()
    {
        collectLatestFlowWhenStarted(viewModel.eventFlow)
        {
            when (it)
            {
                is TestConfigurationEvent.OnPlay             ->
                {
                    navigate(
                        R.id.action_testConfigurationFragment_to_testFragment,
                        bundleOf(*it.args.toTypedArray())
                    )
                }
                is TestConfigurationEvent.OnShowErrorMessage -> toast(it.error.asString(requireContext()))
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
            setMinValueError(when (it.error)
            {
                is ConfigurationError.ValueEmpty -> getString(R.string.error_cant_be_empty)
                else                             -> null
            })
        }
        collectLatestFlowWhenStarted(viewModel.maxValueState)
        {
            setMaxValueError(when (it.error)
            {
                is ConfigurationError.ValueEmpty -> getString(R.string.error_cant_be_empty)
                else                             -> null
            })
        }
        collectLatestFlowWhenStarted(viewModel.testCountOrTimeState)
        {
            setTimeError(when (it.error)
            {
                is ConfigurationError.ValueEmpty -> getString(R.string.error_cant_be_empty)
                else                             -> null
            })
        }
    }

    private fun setMinValueError(error: String?)
    {
        binding.tietMinValue.error = error
    }

    private fun setMaxValueError(error: String?)
    {
        binding.tietMaxValue.error = error
    }

    private fun setTimeError(error: String?)
    {
        binding.etTime.error = error
    }

    //endregion
}
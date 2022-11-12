package com.elian.computeit.feature_tests.presentation.test_configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.elian.computeit.R
import com.elian.computeit.core.util.Error
import com.elian.computeit.core.util.extensions.*
import com.elian.computeit.databinding.FragmentTestConfigurationBinding
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationAction.*
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationEvent.OnShowErrorMessage
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationEvent.OnStart
import com.elian.computeit.feature_tests.presentation.util.TestConfigurationError
import com.google.android.material.radiobutton.MaterialRadioButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
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

        // For some reason if I don't add this line then when going from TestEndFragment to HomeFragment
        // navigate up with the back button doesn't work in any fragment.
        onBackPressed { findNavController().navigateUp() }
    }


    private fun initUI()
    {
        binding.apply()
        {
            tietMinValue.onTextChangedClearError()
            tietMaxValue.onTextChangedClearError()
            etTime.onTextChangedClearError()

            val operationTypeList = rgOperationType.findViewsWithTagOfType<MaterialRadioButton>(R.string.tag_operation_type)

            viewModel.onAction(SelectOperationType(
                symbol = operationTypeList.first().text.toString()
            ))

            operationTypeList.forEach { radioButton ->

                radioButton.setOnClickListener()
                {
                    viewModel.onAction(SelectOperationType(
                        symbol = radioButton.text.toString()
                    ))
                }
            }

            btnStart.setOnClickListener()
            {
                viewModel.onAction(EnterSeconds(etTime.text.toString().toIntOrNull()))

                viewModel.onAction(EnterRange(
                    min = tietMinValue.text.toString().toIntOrNull(),
                    max = tietMaxValue.text.toString().toIntOrNull()
                ))

                viewModel.onAction(Start)
            }
        }
    }

    private fun subscribeToEvents()
    {
        collectLatestFlowWhenStarted(viewModel.eventFlow)
        {
            when (it)
            {
                is OnStart            ->
                {
                    navigateSafe(
                        action = R.id.action_testConfigurationFragment_to_testFragment,
                        args = bundleOf(*it.args.toTypedArray()),
                        currentDestination = R.id.testConfigurationFragment,
                    )
                }
                is OnShowErrorMessage -> toast(it.error.asString(context))
            }
        }
        collectLatestFlowWhenStarted(viewModel.minValueState.map { it.error })
        {
            binding.tietMinValue.error = getFieldError(it)
        }
        collectLatestFlowWhenStarted(viewModel.maxValueState.map { it.error })
        {
            binding.tietMaxValue.error = getFieldError(it)
        }
        collectLatestFlowWhenStarted(viewModel.testTimeState.map { it.error })
        {
            binding.etTime.error = getFieldError(it)
        }
    }

    private fun getFieldError(error: Error?) = when (error)
    {
        is TestConfigurationError.Empty -> getString(R.string.error_cant_be_empty)
        else                            -> null
    }
}
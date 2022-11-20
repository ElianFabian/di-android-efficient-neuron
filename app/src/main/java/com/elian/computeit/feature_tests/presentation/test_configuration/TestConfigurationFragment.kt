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
import com.elian.computeit.core.presentation.util.extensions.collectLatestFlowWhenStarted
import com.elian.computeit.core.presentation.util.extensions.findViewsWithTagOfType
import com.elian.computeit.core.presentation.util.extensions.navigateSafe
import com.elian.computeit.core.presentation.util.extensions.toast
import com.elian.computeit.core.util.Error
import com.elian.computeit.core.util.extensions.apply2
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
    }


    private fun initUI() = binding.apply2()
    {
        val operationTypeList = rgOperationType.findViewsWithTagOfType<MaterialRadioButton>(R.string.tag_operation_type)

        viewModel.onAction(SelectOperationType(symbol = operationTypeList.first().text.toString()))

        operationTypeList.forEach { radioButton ->

            radioButton.setOnClickListener()
            {
                viewModel.onAction(SelectOperationType(symbol = radioButton.text.toString()))
            }
        }

        tietMinValue.addTextChangedListener { viewModel.onAction(EnterMinValue(it.toString().toIntOrNull())) }
        tietMaxValue.addTextChangedListener { viewModel.onAction(EnterMaxValue(it.toString().toIntOrNull())) }
        tietTestTime.addTextChangedListener { viewModel.onAction(EnterTestTime(it.toString().toIntOrNull())) }

        btnStart.setOnClickListener { viewModel.onAction(Start) }
    }

    private fun subscribeToEvents() = viewModel.apply2()
    {
        collectLatestFlowWhenStarted(eventFlow)
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
        collectLatestFlowWhenStarted(minValueState.map { it.error }) { binding.tietMinValue.error = getFieldError(it) }
        collectLatestFlowWhenStarted(maxValueState.map { it.error }) { binding.tietMaxValue.error = getFieldError(it) }
        collectLatestFlowWhenStarted(testTimeState.map { it.error }) { binding.tietTestTime.error = getFieldError(it) }
    }

    private fun getFieldError(error: Error?) = when (error)
    {
        is TestConfigurationError.Empty -> getString(R.string.error_cant_be_empty)
        else                            -> null
    }
}
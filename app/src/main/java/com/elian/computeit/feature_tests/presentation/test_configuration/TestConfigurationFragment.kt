package com.elian.computeit.feature_tests.presentation.test_configuration

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elian.computeit.R
import com.elian.computeit.core.domain.states.NumericFieldError
import com.elian.computeit.core.presentation.util.extensions.*
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.Error
import com.elian.computeit.core.util.extensions.apply2
import com.elian.computeit.databinding.FragmentTestConfigurationBinding
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationAction.*
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationEvent.OnShowErrorMessage
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationEvent.OnStart
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class TestConfigurationFragment : Fragment(R.layout.fragment_test_configuration)
{
	private val viewModel by viewModels<TestConfigurationViewModel>()
	private val binding by viewBinding(FragmentTestConfigurationBinding::bind)

	private lateinit var _lastCheckedOperationRadioButton: RadioButton


	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		subscribeToEvents()
		initUI()
	}


	private fun initUI() = binding.apply2()
	{
		rgOperationType.findViewsWithTagOfType<RadioButton>(R.string.tag_operation_type).forEach { radioButton ->

			radioButton.setOnClickListener()
			{
				_lastCheckedOperationRadioButton = radioButton

				viewModel.onAction(SelectOperationType(symbol = radioButton.text.toString()))
			}
		}

		// This is to avoid problems when saving the RadioButton checked state after navigating up from test to here
		if (!::_lastCheckedOperationRadioButton.isInitialized)
		{
			_lastCheckedOperationRadioButton = rgOperationType.checkedRadioButton!!
		}
		_lastCheckedOperationRadioButton.performClick()

		tietStart.addTextChangedListener { viewModel.onAction(EnterStart(it.toString().toIntOrNull())) }
		tietEnd.addTextChangedListener { viewModel.onAction(EnterEnd(it.toString().toIntOrNull())) }
		tietTime.addTextChangedListener { viewModel.onAction(EnterTime(it.toString().toIntOrNull())) }

		btnStartTest.setOnClickListener { viewModel.onAction(StartTest) }
	}

	private fun subscribeToEvents() = viewModel.apply2()
	{
		collectFlowWhenStarted(eventFlow)
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
				is OnShowErrorMessage -> showToast(it.error.asString(context))
			}
		}
		collectLatestFlowWhenStarted(startState.map { it.error }) { binding.tietStart.error = getFieldError(it) }
		collectLatestFlowWhenStarted(endState.map { it.error }) { binding.tietEnd.error = getFieldError(it) }
		collectLatestFlowWhenStarted(timeState.map { it.error }) { binding.tietTime.error = getFieldError(it) }
	}

	private fun getFieldError(error: Error?) = when (error)
	{
		is NumericFieldError.Empty -> getString(R.string.error_cant_be_empty)
		else                       -> null
	}
}
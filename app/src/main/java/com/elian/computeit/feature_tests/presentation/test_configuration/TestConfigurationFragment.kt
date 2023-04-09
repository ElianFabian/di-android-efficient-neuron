package com.elian.computeit.feature_tests.presentation.test_configuration

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elian.computeit.R
import com.elian.computeit.core.domain.errors.NumericFieldError
import com.elian.computeit.core.presentation.util.extensions.*
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.Error
import com.elian.computeit.core.util.constants.toBundle
import com.elian.computeit.core.util.using
import com.elian.computeit.databinding.FragmentTestConfigurationBinding
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationAction.*
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationEvent.OnShowErrorMessage
import com.elian.computeit.feature_tests.presentation.test_configuration.TestConfigurationEvent.OnStartTest
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestConfigurationFragment : Fragment(R.layout.fragment_test_configuration)
{
	private val viewModel by viewModels<TestConfigurationViewModel>()
	private val binding by viewBinding(FragmentTestConfigurationBinding::bind)


	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		subscribeToEvents()
		initializeUi()
	}


	private fun initializeUi() = using(binding)
	{
		val operationRadioButtons = binding.rgOperationType.findViewsWithTagOfType<RadioButton>(R.string.tag_operation_type)

		operationRadioButtons.forEach { radioButton ->

			radioButton.setOnClickListener()
			{
				viewModel.onAction(SelectOperationType(symbol = radioButton.text.toString()))
			}
		}
		val selectedOperationRadioButton = operationRadioButtons.first { rb -> rb.text == viewModel.state.value.selectedOperation.symbol }
		selectedOperationRadioButton.performClick()

		tietStartOfRange.addTextChangedListener { viewModel.onAction(EnterStartOfRange(it.toString().toIntOrNull())) }
		tietEndOfRange.addTextChangedListener { viewModel.onAction(EnterEndOfRange(it.toString().toIntOrNull())) }
		tietTime.addTextChangedListener { viewModel.onAction(EnterTime(it.toString().toIntOrNull())) }

		btnStartTest.setOnClickListener { viewModel.onAction(StartTest) }
	}

	private fun subscribeToEvents() = using(viewModel)
	{
		collectLatestFlowWhenStarted(state)
		{
			binding.apply()
			{
				tietStartOfRange.setTextIfDistinct("${it.startOfRange ?: ""}")
				tietEndOfRange.setTextIfDistinct("${it.endOfRange ?: ""}")

				tietStartOfRange.error = getFieldError(it.startOfRangeError)
				tietEndOfRange.error = getFieldError(it.endOfRangeError)
				tietTime.error = getFieldError(it.timeError)
			}
		}
		collectFlowWhenStarted(eventFlow)
		{
			when (it)
			{
				is OnStartTest        ->
				{
					navigateSafe(
						action = R.id.action_testConfigurationFragment_to_testFragment,
						args = it.args.toBundle(),
						currentDestination = R.id.testConfigurationFragment,
					)
				}
				is OnShowErrorMessage -> when (val errorMessage = it.error.asString(context))
				{
					getString(R.string.error_range_values_are_inverted) ->
					{
						Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).setAction(R.string.action_fix)
						{
							viewModel.onAction(SwapToFixRangeBounds)
						}.show()
					}
					else                                                ->
					{
						showToast(errorMessage)
					}
				}
			}
		}
	}

	private fun getFieldError(error: Error?) = when (error)
	{
		is NumericFieldError.Empty -> getString(R.string.error_cant_be_empty)
		else                       -> null
	}
}
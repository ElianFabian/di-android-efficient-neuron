package com.elian.computeit.feature_tests.presentation.test_configuration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.elian.computeit.R
import com.elian.computeit.core.domain.errors.NumericFieldError
import com.elian.computeit.core.domain.models.OperationType
import com.elian.computeit.core.presentation.util.extensions.onClick
import com.elian.computeit.core.presentation.util.extensions.onTextChanged
import com.elian.computeit.core.presentation.util.extensions.setTextIfDistinct
import com.elian.computeit.core.presentation.util.extensions.showSnackBar
import com.elian.computeit.core.presentation.util.extensions.showToast
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.Error
import com.elian.computeit.core.util.asString
import com.elian.computeit.core.util.constants.toBundle
import com.elian.computeit.databinding.FragmentTestConfigurationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TestConfigurationFragment : Fragment(R.layout.fragment_test_configuration) {

	private val viewModel by viewModels<TestConfigurationViewModel>()
	private val binding by viewBinding(FragmentTestConfigurationBinding::bind)
	private val navController by lazy { findNavController() }


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		subscribeToEvents()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initializeUi()
	}


	private fun initializeUi() {
		binding.apply {
			rbAddition.onClick {
				viewModel.onAction(TestConfigurationAction.SelectOperation(OperationType.Addition))
			}
			rbSubtraction.onClick {
				viewModel.onAction(TestConfigurationAction.SelectOperation(OperationType.Subtraction))
			}
			rbMultiplication.onClick {
				viewModel.onAction(TestConfigurationAction.SelectOperation(OperationType.Multiplication))
			}
			rbDivision.onClick {
				viewModel.onAction(TestConfigurationAction.SelectOperation(OperationType.Division))
			}

			tietStartOfRange.onTextChanged { start ->
				viewModel.onAction(TestConfigurationAction.EnterStartOfRange(start.toIntOrNull()))
			}
			tietEndOfRange.onTextChanged { end ->
				viewModel.onAction(TestConfigurationAction.EnterEndOfRange(end.toIntOrNull()))
			}
			tietTime.onTextChanged { time ->
				viewModel.onAction(TestConfigurationAction.EnterTime(time.toIntOrNull()))
			}

			btnStartTest.onClick {
				viewModel.onAction(TestConfigurationAction.StartTest)
			}
		}
	}

	private fun subscribeToEvents() {
		lifecycleScope.launch {
			viewModel.state.flowWithLifecycle(lifecycle)
				.collectLatest { state ->
					binding.apply {
						tietStartOfRange.setTextIfDistinct("${state.startOfRange ?: ""}")
						tietEndOfRange.setTextIfDistinct("${state.endOfRange ?: ""}")

						tietStartOfRange.error = getFieldError(state.startOfRangeError)
						tietEndOfRange.error = getFieldError(state.endOfRangeError)
						tietTime.error = getFieldError(state.timeError)

						rgOperationType.apply {
							clearCheck()
							when (state.selectedOperation) {
								OperationType.Addition       -> check(rbAddition.id)
								OperationType.Subtraction    -> check(rbSubtraction.id)
								OperationType.Multiplication -> check(rbMultiplication.id)
								OperationType.Division       -> check(rbDivision.id)
							}
						}
					}
				}
		}
		lifecycleScope.launch {
			viewModel.eventFlow.flowWithLifecycle(lifecycle)
				.collect { event ->
					when (event) {
						is TestConfigurationEvent.OnStartTest   -> {
							navController.navigate(
								resId = R.id.action_testConfigurationFragment_to_testFragment,
								args = event.args.toBundle(),
							)
						}
						is TestConfigurationEvent.OnShowMessage -> {
							if (event.action == null) {
								showToast(event.message.asString(context))
							}
							else {
								showSnackBar(
									message = event.message.asString(context),
									actionName = getString(R.string.action_fix),
									action = {
										event.action.invoke()
									}
								)
							}
						}
					}
				}
		}
	}

	private fun getFieldError(error: Error?) = when (error) {
		is NumericFieldError.Empty -> getString(R.string.error_cant_be_empty)
		else                       -> null
	}
}
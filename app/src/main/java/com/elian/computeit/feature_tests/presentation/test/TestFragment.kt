package com.elian.computeit.feature_tests.presentation.test

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.findViewsOfTypeWithTag
import com.elian.computeit.core.presentation.util.extensions.onClick
import com.elian.computeit.core.presentation.util.extensions.onClickOnce
import com.elian.computeit.core.presentation.util.extensions.startAlphaAnimation
import com.elian.computeit.core.presentation.util.extensions.textSizeInSp
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.constants.arguments
import com.elian.computeit.core.util.constants.toBundle
import com.elian.computeit.databinding.FragmentTestBinding
import com.elian.computeit.feature_tests.domain.args.TestArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TestFragment : Fragment(R.layout.fragment_test) {

	private val viewModel by viewModels<TestViewModel>()
	private val binding by viewBinding(FragmentTestBinding::bind)
	private val navController by lazy { findNavController() }
	private val args by arguments<TestArgs>()

	private val operationView by lazy {
		object {
			private val horizontal = binding.viewHorizontalOperation
			private val vertical = binding.viewVerticalOperation

			private val tvFirstPair = listOf(horizontal.tvFirstNumber, vertical.tvFirstNumber)
			private val tvSecondPair = listOf(horizontal.tvSecondNumber, vertical.tvSecondNumber)
			private val tvSymbolPair = listOf(horizontal.tvSymbol, vertical.tvSymbol)
			private val rootPair = listOf(horizontal.root, vertical.root)

			var firstNumber: String
				get() = tvFirstPair.first().text.toString()
				set(value) = tvFirstPair.forEach { it.text = value }

			var secondNumber: String
				get() = tvSecondPair.first().text.toString()
				set(value) = tvSecondPair.forEach { it.text = value }

			var symbol: String
				get() = tvSymbolPair.first().text.toString()
				set(value) = tvSymbolPair.forEach { it.text = value }

			fun toggleDistribution() = rootPair.forEach { it.isGone = !it.isGone }
		}
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		subscribeToEvents()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initUi()
	}


	private fun initUi() {
		disableScreenInteraction()

		binding.apply {
			tvRemainingSeconds.apply {
				if (viewModel.state.value.testType == TestType.INFINITE) {
					textSizeInSp = resources.getDimension(R.dimen.textSize_xlarge1)
				}
				onClick {
					viewModel.onAction(TestAction.ForceFinish)
				}
			}

			operationView.symbol = args.operation.symbol

			llKeyBoard.findViewsOfTypeWithTag<Button>(R.string.tag_numeric_button).forEach { button ->
				button.onClick {
					viewModel.onAction(TestAction.EnterDigit(button.text.toString().toInt()))
				}
			}

			flOperation.onClick { operationView.toggleDistribution() }

			btnRemoveLastDigit.onClick { viewModel.onAction(TestAction.RemoveLastDigit) }
			btnNextOperation.onClick { viewModel.onAction(TestAction.NextOperation) }
			btnClearInput.onClick { viewModel.onAction(TestAction.ClearInput) }

			clTouchToStart.onClickOnce {
				val transitionDuration = 600L

				clTouchToStart.startAlphaAnimation(
					fromAlpha = 1F,
					toAlpha = 0F,
					durationMillis = transitionDuration,
				)

				lifecycleScope.launch {
					delay(transitionDuration + 150L)

					viewModel.onAction(TestAction.StartTest)

					enableScreenInteraction()
				}
			}
		}
	}

	private fun subscribeToEvents() {
		lifecycleScope.launch {
			viewModel.state.flowWithLifecycle(lifecycle)
				.collectLatest { state ->
					val pair = state.pairOfNumbers
					val testType = state.testType

					operationView.apply {
						firstNumber = pair?.first?.toString() ?: "?"
						secondNumber = pair?.second?.toString() ?: "?"
						symbol = state.operation.symbol
					}

					binding.apply {
						tietInput.apply {
							setText("${state.insertedResult}")
						}
						pbRemainingSeconds.apply {
							max = when (testType) {
								TestType.FINITE   -> state.totalTimeInMillis.toInt()
								TestType.INFINITE -> 1
							}
							if (testType == TestType.INFINITE) {
								progress = 1
							}
						}
					}
				}
		}
		lifecycleScope.launch {
			viewModel.formattedTimeState.flowWithLifecycle(lifecycle)
				.collectLatest { formattedTime ->
					binding.tvRemainingSeconds.text = formattedTime
				}
		}
		lifecycleScope.launch {
			viewModel.timerMillisState.flowWithLifecycle(lifecycle)
				.collectLatest { millis ->
					val testType = viewModel.state.value.testType

					if (testType == TestType.FINITE) {
						binding.pbRemainingSeconds.progress = millis.toInt()
					}
				}
		}
		lifecycleScope.launch {
			viewModel.eventFlow.flowWithLifecycle(lifecycle)
				.collectLatest { event ->
					when (event) {
						is TestEvent.OnTimerFinish     -> {
							disableScreenInteraction()
						}
						is TestEvent.OnGoToTestDetails -> {
							navController.navigate(
								resId = R.id.action_testFragment_to_testDetailsFragment,
								args = event.args.toBundle(),
							)
						}
					}
				}
		}
	}

	private fun enableScreenInteraction() {
		binding.clTouchToStart.isClickable = false
	}

	private fun disableScreenInteraction() {
		binding.clTouchToStart.isClickable = true
	}
}
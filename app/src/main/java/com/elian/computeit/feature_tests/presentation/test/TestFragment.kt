package com.elian.computeit.feature_tests.presentation.test

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.presentation.util.extensions.*
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.constants.TestArgKeys
import com.elian.computeit.core.util.extensions.apply2
import com.elian.computeit.core.util.extensions.format
import com.elian.computeit.databinding.FragmentTestBinding
import com.elian.computeit.feature_tests.presentation.test.TestAction.*
import com.elian.computeit.feature_tests.presentation.test.TestEvent.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TestFragment : Fragment(R.layout.fragment_test)
{
	private val viewModel by viewModels<TestViewModel>()
	private val binding by viewBinding(FragmentTestBinding::bind)

	private val operationView by lazy {
		object
		{
			private val horizontalOperation = binding.lytHorizontalOperation
			private val verticalOperation = binding.lytVerticalOperation

			var firstNumber: String
				get() = horizontalOperation.tvFirstNumber.text.toString()
				set(value)
				{
					horizontalOperation.tvFirstNumber.text = value
					verticalOperation.tvFirstNumber.text = value
				}
			var secondNumber: String
				get() = horizontalOperation.tvSecondNumber.text.toString()
				set(value)
				{
					horizontalOperation.tvSecondNumber.text = value
					verticalOperation.tvSecondNumber.text = value
				}
			var symbol: String
				get() = horizontalOperation.tvSymbol.text.toString()
				set(value)
				{
					horizontalOperation.tvSymbol.text = value
					verticalOperation.tvSymbol.text = value
				}

			fun toggleDistribution()
			{
				if (horizontalOperation.root.isGone)
				{
					horizontalOperation.root.isVisible = true
					verticalOperation.root.isGone = true
				}
				else
				{
					verticalOperation.root.isVisible = true
					horizontalOperation.root.isGone = true
				}
			}
		}
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		subscribeToEvents()
		initUi()
	}


	private fun initUi() = binding.apply2()
	{
		disableScreenInteraction()

		arguments?.getInt(TestArgKeys.TestTimeInSeconds)!!.also()
		{
			mtvRemainingSeconds.text = if (it == 0)
			{
				mtvRemainingSeconds.textSizeScaleDensity = resources.getDimension(R.dimen.textSize_xlarge1)

				mtvRemainingSeconds.setOnClickListener { viewModel.onAction(ForceFinish) }

				"∞"
			}
			else it.toFloat().format("%.1f")

			val initialProgress = if (it == 0) 1 else it * 1_000

			cpiRemainingSeconds.max = initialProgress
			cpiRemainingSeconds.progress = initialProgress
		}
		arguments?.getSerializable(TestArgKeys.OperationType)!!.let { it as Operation }.also()
		{
			operationView.symbol = it.symbol
		}

		llKeyBoard.findViewsWithTagOfType<Button>(R.string.tag_numeric_button).forEach { button ->

			button.setOnClickListener()
			{
				viewModel.onAction(EnterNumber(button.text.toString().toInt()))
			}
		}

		mFlOperation.setOnClickListener { operationView.toggleDistribution() }

		btnRemoveLastDigit.setOnClickListener { viewModel.onAction(RemoveLastDigit) }
		btnNextTest.setOnClickListener { viewModel.onAction(NextTest) }
		btnClearInput.setOnClickListener { viewModel.onAction(ClearInput) }

		clTouchToStart.setOnClickListenerOnlyOnce()
		{
			val transitionDuration = 600L

			clTouchToStart.startAlphaAnimation(
				fromAlpha = 1F,
				toAlpha = 0F,
				duration = transitionDuration,
			)

			lifecycleScope.launch()
			{
				delay(transitionDuration + 150L)

				viewModel.startTimer()

				enableScreenInteraction()
			}
		}
	}

	private fun subscribeToEvents() = viewModel.apply2()
	{
		collectFlowWhenStarted(eventFlow)
		{
			when (it)
			{
				is OnTimerTickInNormalMode   ->
				{
					val seconds = it.millisUntilFinished / 1000F

					binding.cpiRemainingSeconds.progress = it.millisUntilFinished.toInt()
					binding.mtvRemainingSeconds.text = seconds.format("%.1f")
				}
				is OnTimerTickInInfiniteMode ->
				{
				}
				is OnTimerFinish             ->
				{
					disableScreenInteraction()
				}
				is OnGoToTestDetails         ->
				{
					navigate(R.id.action_testFragment_to_testDetailsFragment, bundleOf(*it.args.toTypedArray()))
				}
			}
		}
		collectLatestFlowWhenStarted(resultState)
		{
			binding.tietInput.setText(it.toString())
		}
		collectLatestFlowWhenStarted(pairOfNumbersState.filterNotNull())
		{
			operationView.firstNumber = it.first.toString()
			operationView.secondNumber = it.second.toString()
		}
		collectLatestFlowWhenStarted(operationSymbolState) { operationView.symbol = it }
	}

	private fun enableScreenInteraction()
	{
		binding.clTouchToStart.isClickable = false
	}

	private fun disableScreenInteraction()
	{
		binding.clTouchToStart.isClickable = true
	}
}
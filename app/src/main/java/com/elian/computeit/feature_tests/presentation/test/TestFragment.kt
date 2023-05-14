package com.elian.computeit.feature_tests.presentation.test

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.*
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.constants.arguments
import com.elian.computeit.core.util.constants.toBundle
import com.elian.computeit.core.util.extensions.format
import com.elian.computeit.core.util.using
import com.elian.computeit.databinding.FragmentTestBinding
import com.elian.computeit.feature_tests.domain.args.TestArgs
import com.elian.computeit.feature_tests.presentation.test.TestAction.*
import com.elian.computeit.feature_tests.presentation.test.TestEvent.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TestFragment : Fragment(R.layout.fragment_test)
{
	private val viewModel by viewModels<TestViewModel>()
	private val binding by viewBinding(FragmentTestBinding::bind)
	private val args by arguments<TestArgs>()

	private val operationView by lazy {
		object
		{
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


	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		subscribeToEvents()
		initializeUi()
	}


	private fun initializeUi() = using(binding)
	{
		disableScreenInteraction()

		args.totalTimeInSeconds.also()
		{
			mtvRemainingSeconds.text = if (it == 0)
			{
				mtvRemainingSeconds.textSizeInSp = resources.getDimension(R.dimen.textSize_xlarge1)

				mtvRemainingSeconds.setOnClickListener { viewModel.onAction(ForceFinish) }

				"∞"
			}
			else it.toFloat().format("%.1f")

			val initialProgress = if (it == 0) 1 else it * 1_000

			cpiRemainingSeconds.max = initialProgress
			cpiRemainingSeconds.progress = initialProgress
		}

		operationView.symbol = args.operation.symbol

		llKeyBoard.findViewsOfTypeWithTag<Button>(R.string.tag_numeric_button).forEach { button ->

			button.setOnClickListener()
			{
				viewModel.onAction(EnterNumber(button.text.toString().toInt()))
			}
		}

		flOperation.setOnClickListener { operationView.toggleDistribution() }

		btnRemoveLastDigit.setOnClickListener { viewModel.onAction(RemoveLastDigit) }
		btnNextOperation.setOnClickListener { viewModel.onAction(NextOperation) }
		btnClearInput.setOnClickListener { viewModel.onAction(ClearInput) }

		clTouchToStart.setOnClickListenerOnlyOnce()
		{
			val transitionDuration = 600L

			clTouchToStart.startAlphaAnimation(
				fromAlpha = 1F,
				toAlpha = 0F,
				durationMillis = transitionDuration,
			)

			lifecycleScope.launch()
			{
				delay(transitionDuration + 150L)

				viewModel.startTimer()

				enableScreenInteraction()
			}
		}
	}

	private fun subscribeToEvents() = using(viewModel)
	{
		collectLatestFlowWhenStarted(state)
		{
			it.pairOfNumbers?.also { pair ->

				operationView.firstNumber = pair.first.toString()
				operationView.secondNumber = pair.second.toString()
			}
			binding.tietInput.setText("${it.insertedResult}")
			operationView.symbol = it.operationSymbol
		}
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
					// this may be used in future
				}
				is OnTimerFinish             ->
				{
					disableScreenInteraction()
				}
				is OnGoToTestDetails         ->
				{
					navigate(R.id.action_testFragment_to_testDetailsFragment, it.args.toBundle())
				}
			}
		}
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
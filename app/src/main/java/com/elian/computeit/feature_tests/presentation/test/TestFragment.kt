package com.elian.computeit.feature_tests.presentation.test

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.presentation.util.extensions.*
import com.elian.computeit.core.presentation.util.isScreenOn
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.constants.EXTRA_OPERATION_TYPE
import com.elian.computeit.core.util.constants.EXTRA_TEST_TIME_IN_SECONDS
import com.elian.computeit.core.util.extensions.apply2
import com.elian.computeit.core.util.extensions.format
import com.elian.computeit.databinding.FragmentTestBinding
import com.elian.computeit.feature_tests.presentation.test.TestAction.*
import com.elian.computeit.feature_tests.presentation.test.TestEvent.OnTimerFinish
import com.elian.computeit.feature_tests.presentation.test.TestEvent.OnTimerTick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TestFragment : Fragment(R.layout.fragment_test)
{
	private val viewModel by viewModels<TestViewModel>()
	private val binding by viewBinding(FragmentTestBinding::bind)

	private var _hasTestStarted = false


	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		initUi()
		subscribeToEvents()
	}

	override fun onPause()
	{
		super.onPause()

		if (_hasTestStarted && !isScreenOn(context)) navigateUp()
	}


	private fun initUi() = binding.apply2()
	{
		disableScreenInteraction()

		arguments?.getInt(EXTRA_TEST_TIME_IN_SECONDS)!!.also()
		{
			mtvRemainingSeconds.text = it.toFloat().format("%.1f")

			cpiRemainingSeconds.max = it * 1_000
			cpiRemainingSeconds.progress = it * 1_000
		}
		arguments?.getSerializable(EXTRA_OPERATION_TYPE)!!.let { it as Operation }.also()
		{
			tvOperationSymbol.text = it.symbol
		}

		llKeyBoard.findViewsWithTagOfType<Button>(R.string.tag_numeric_button).forEach { button ->

			button.setOnClickListener()
			{
				viewModel.onAction(EnterNumber(button.text.toString().toInt()))
			}
		}

		btnNextTest.setOnClickListener { viewModel.onAction(NextTest) }
		btnClearInput.setOnClickListener { viewModel.onAction(ClearInput) }

		clTouchToStart.setOnClickListenerOnlyOnce()
		{
			_hasTestStarted = true

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

	private fun subscribeToEvents() = viewModel.apply()
	{
		collectFlowWhenStarted(eventFlow)
		{
			when (it)
			{
				is OnTimerTick   ->
				{
					val seconds = it.millisUntilFinished / 1000F

					binding.cpiRemainingSeconds.progress = it.millisUntilFinished.toInt()
					binding.mtvRemainingSeconds.text = seconds.format("%.1f")
				}
				is OnTimerFinish ->
				{
					disableScreenInteraction()

					navigate(R.id.action_testFragment_to_testEndFragment, bundleOf(*it.args.toTypedArray()))
				}
			}
		}
		collectLatestFlowWhenStarted(resultState)
		{
			binding.tietInput.setText(it.toString())
		}
		collectLatestFlowWhenStarted(pairOfNumbersState.filterNotNull())
		{
			binding.tvFirstNumber.text = it.first.toString()
			binding.tvSecondNumber.text = it.second.toString()
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
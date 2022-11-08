package com.elian.computeit.feature_tests.presentation.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.presentation.MainActivity
import com.elian.computeit.core.util.constants.EXTRA_OPERATION_TYPE
import com.elian.computeit.core.util.extensions.*
import com.elian.computeit.databinding.FragmentTestBinding
import com.elian.computeit.feature_tests.presentation.test.TestAction.*
import com.elian.computeit.feature_tests.presentation.test.TestEvent.OnTimerFinish
import com.elian.computeit.feature_tests.presentation.test.TestEvent.OnTimerTick
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TestFragment : Fragment()
{
    private val viewModel by viewModels<TestViewModel>()
    private lateinit var binding: FragmentTestBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentTestBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        subscribeToEvents()
    }

    private fun initUi()
    {
        (activity as MainActivity).disableDrawerLayout()

        binding.apply()
        {
            arguments?.getParcelable<Operation>(EXTRA_OPERATION_TYPE)!!.also()
            {
                tvOperationSymbol.text = it.symbol
            }

            llKeyBoard.findViewsWithTagOfType<MaterialButton>(R.string.tag_numeric_button).forEach { button ->

                button.setOnClickListener()
                {
                    viewModel onAction EnterNumber(
                        value = button.text.toString().toInt()
                    )
                }
            }

            mtvRemainingSeconds.text = viewModel.millisInFuture.fromMillisToSeconds().toString()
            cpiRemainingSeconds.apply()
            {
                max = viewModel.millisInFuture.toInt()
                progress = viewModel.millisInFuture.toInt()
            }

            btnNextTest.setOnClickListener { viewModel onAction NextTest }
            btnClearInput.setOnClickListener { viewModel onAction ClearInput }

            clMain.isClickable = false

            clTouchToStart.setOnClickListener()
            {
                clMain.isClickable = true
                clTouchToStart.isClickable = false

                val transitionDuration = 600L

                val fadeOutAnimation = AlphaAnimation(1f, 0f).apply()
                {
                    duration = transitionDuration
                    fillAfter = true
                }

                clTouchToStart.startAnimation(fadeOutAnimation)

                lifecycleScope.launch()
                {
                    delay(transitionDuration + 150L)
                    viewModel.startTimer()
                }
            }
        }
    }

    private fun subscribeToEvents()
    {
        collectLatestFlowWhenStarted(viewModel.eventFlow)
        {
            when (it)
            {
                is OnTimerTick   ->
                {
                    val seconds = it.millisUntilFinished.fromMillisToSeconds()

                    binding.apply()
                    {
                        cpiRemainingSeconds.progress = it.millisUntilFinished.toInt()
                        mtvRemainingSeconds.text = seconds.format("%.1f")
                    }
                }
                is OnTimerFinish ->
                {
                    navigate(R.id.action_testFragment_to_testEndFragment, bundleOf(*it.args.toTypedArray()))
                }
            }
        }
        collectLatestFlowWhenStarted(viewModel.resultState)
        {
            binding.tietInput.setText(it.toString())
        }
        collectLatestFlowWhenStarted(viewModel.pairOfNumbersState)
        {
            if (it == null) return@collectLatestFlowWhenStarted

            binding.tvFirstNumber.text = it.first.toString()
            binding.tvSecondNumber.text = it.second.toString()
        }
    }
}
package com.elian.computeit.feature_tests.presentation.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.util.extensions.*
import com.elian.computeit.databinding.FragmentTestBinding
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestFragment : Fragment()
{
    private lateinit var binding: FragmentTestBinding
    private val viewModel by viewModels<TestViewModel>()


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
        initTimer()
    }

    private fun initUi()
    {
        binding.apply()
        {
            val numericButtons = llKeyBoard.findViewsWithTagOfType<MaterialButton>(R.string.tag_numeric_button)

            numericButtons.forEach { button ->
                button.setOnClickListener()
                {
                    onActionWhenStarted(TestAction.EnteredNumber(value = button.text.toString().toInt()))
                }
            }

            mtvRemainingSeconds.text = viewModel.millisInFuture.fromMillisToSeconds().toString()
            cpiRemainingSeconds.apply()
            {
                progress = viewModel.millisInFuture.toInt()
                max = viewModel.millisInFuture.toInt()
            }

            btnNextTest.setOnClickListener { onActionWhenStarted(TestAction.NextTest) }
            btnClearInput.setOnClickListener { onActionWhenStarted(TestAction.ClearInput) }
        }
    }

    private fun initTimer()
    {
        viewModel.startTimer()
    }

    private fun subscribeToEvents()
    {
        collectLatestFlowWhenStarted(viewModel.eventFlow)
        {
            when (it)
            {
                is TestEvent.TimerTicked   ->
                {
                    val seconds = it.millisUntilFinished.fromMillisToSeconds()

                    binding.apply()
                    {
                        cpiRemainingSeconds.progress = it.millisUntilFinished.toInt()
                        mtvRemainingSeconds.text = seconds.format("%.1f")
                    }
                }
                is TestEvent.TimerFinished ->
                {
                    toast("Finished")
                }
            }
        }
        collectLatestFlowWhenStarted(viewModel.resultState)
        {
            binding.tietInput.setText(it.toString())
        }
        collectLatestFlowWhenStarted(viewModel.pairOfNumbersState)
        {
            binding.tvFirstNumber.text = it.first.toString()
            binding.tvSecondNumber.text = it.second.toString()
        }
    }

    private fun onActionWhenStarted(action: TestAction)
    {
        lifecycleScope.launchWhenStarted { viewModel.onAction(action) }
    }
}
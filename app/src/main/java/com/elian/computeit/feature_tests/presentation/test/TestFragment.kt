package com.elian.computeit.feature_tests.presentation.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.util.extensions.collectLatestFlowWhenStarted
import com.elian.computeit.core.util.extensions.findViewsWithTagOfType
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
    }

    private fun initUi()
    {
        val numericButtons = binding.llKeyBoard.findViewsWithTagOfType<MaterialButton>(R.string.tag_numeric_button)

        numericButtons.forEach { button ->
            button.setOnClickListener()
            {
                onActionWhenStarted(TestAction.EnteredNumber(value = button.text.toString().toInt()))
            }
        }

        viewModel.initializeTimer(10_000L, 1_000L)

        viewModel.startTimer()

        binding.btnNextTest.setOnClickListener { onActionWhenStarted(TestAction.NextTest) }
        binding.btnClearInput.setOnClickListener { onActionWhenStarted(TestAction.ClearInput) }
    }

    private fun subscribeToEvents()
    {
        collectLatestFlowWhenStarted(viewModel.eventFlow)
        {
            when (it)
            {
                is TestEvent.TimerTicked   ->
                {
                    val seconds = it.millisUntilFinished / 1000F
                    
                    //toast("Seconds left: ${seconds}")
                    //println("tick: $seconds")
                }
                is TestEvent.TimerFinished ->
                {
                    //toast("Finished")
                    //println("tick: finished")
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
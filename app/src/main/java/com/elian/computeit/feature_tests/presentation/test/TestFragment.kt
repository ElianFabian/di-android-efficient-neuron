package com.elian.computeit.feature_tests.presentation.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.presentation.MainActivity
import com.elian.computeit.core.util.EXTRA_OPERATION_TYPE
import com.elian.computeit.core.util.extensions.*
import com.elian.computeit.databinding.FragmentTestBinding
import com.elian.computeit.core.data.Operation
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                    onActionWhenStarted(TestAction.EnterNumber(
                        value = button.text.toString().toInt()
                    ))
                }
            }

            mtvRemainingSeconds.text = viewModel.millisInFuture.fromMillisToSeconds().toString()
            cpiRemainingSeconds.apply()
            {
                max = viewModel.millisInFuture.toInt()
                progress = viewModel.millisInFuture.toInt()
            }

            btnNextTest.setOnClickListener { onActionWhenStarted(TestAction.NextTest) }
            btnClearInput.setOnClickListener { onActionWhenStarted(TestAction.ClearInput) }
        }
    }

    private fun initTimer()
    {
        lifecycleScope.launch()
        { 
            delay(1000)
            viewModel.startTimer()
        }
    }

    private fun subscribeToEvents()
    {
        collectLatestFlowWhenStarted(viewModel.eventFlow)
        {
            when (it)
            {
                is TestEvent.OnTimerTick ->
                {
                    val seconds = it.millisUntilFinished.fromMillisToSeconds()
                    println("---------------------${it.millisUntilFinished}")

                    binding.apply()
                    {
                        cpiRemainingSeconds.progress = it.millisUntilFinished.toInt()
                        mtvRemainingSeconds.text = seconds.format("%.1f")
                    }
                }
                is TestEvent.OnTimerFinish ->
                {
                    navigate(R.id.action_testFragment_to_testEndFragment)
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
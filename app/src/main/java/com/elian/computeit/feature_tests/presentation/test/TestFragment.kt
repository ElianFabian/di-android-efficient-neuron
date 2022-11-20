package com.elian.computeit.feature_tests.presentation.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.elian.computeit.R
import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.presentation.util.extensions.*
import com.elian.computeit.core.util.constants.EXTRA_OPERATION_TYPE
import com.elian.computeit.core.util.extensions.format
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
    
    private var _hasTestStarted = false


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

    override fun onPause()
    {
        super.onPause()

        if (_hasTestStarted) findNavController().navigateUp()
    }


    private fun initUi()
    {
        binding.apply()
        {
            disableScreenInteraction()

            arguments?.getSerializable(EXTRA_OPERATION_TYPE)!!.let { it as Operation }.also()
            {
                tvOperationSymbol.text = it.symbol
            }

            llKeyBoard.findViewsWithTagOfType<MaterialButton>(R.string.tag_numeric_button).forEach { button ->

                button.setOnClickListener()
                {
                    viewModel.onAction(EnterNumber(button.text.toString().toInt()))
                }
            }

            mtvRemainingSeconds.text = (viewModel.millisInFuture / 1000F).toString()
            cpiRemainingSeconds.apply()
            {
                max = viewModel.millisInFuture.toInt()
                progress = viewModel.millisInFuture.toInt()
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

                    binding.apply()
                    {
                        cpiRemainingSeconds.progress = it.millisUntilFinished.toInt()
                        mtvRemainingSeconds.text = seconds.format("%.1f")
                    }
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
        collectLatestFlowWhenStarted(pairOfNumbersState)
        {
            if (it == null) return@collectLatestFlowWhenStarted

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
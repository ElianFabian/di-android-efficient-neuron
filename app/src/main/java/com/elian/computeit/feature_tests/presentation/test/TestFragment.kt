package com.elian.computeit.feature_tests.presentation.test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.util.extensions.collectLatestFlowWhenStarted
import com.elian.computeit.core.util.extensions.findViewsWithTagOfType
import com.elian.computeit.databinding.FragmentTestBinding
import com.google.android.material.button.MaterialButton

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

        // TODO: check the format of the input in case the value is out of the int range

        numericButtons.forEach { button ->
            button.setOnClickListener()
            {
                onActionWhenStarted(TestAction.EnteredNumber(value = button.text.toString().toInt()))
            }
        }

        binding.btnNextTest.setOnClickListener { onActionWhenStarted(TestAction.NextTest) }
        binding.btnClearInput.setOnClickListener { onActionWhenStarted(TestAction.ClearInput) }
    }

    private fun subscribeToEvents()
    {
        collectLatestFlowWhenStarted(viewModel.currentNumberState)
        {
            binding.etInput.setText(it.toString())
        }
    }

    private fun onActionWhenStarted(action: TestAction)
    {
        lifecycleScope.launchWhenStarted { viewModel.onAction(action) }
    }
}
package com.elian.computeit.feature_tests.presentation.test_end

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elian.computeit.R
import com.elian.computeit.core.util.extensions.disableNavigateUp
import com.elian.computeit.core.util.extensions.getColorRes
import com.elian.computeit.core.util.extensions.navigate
import com.elian.computeit.core.util.mp_android_chart.applyDefaultStyle
import com.elian.computeit.core.util.mp_android_chart.lineAndCirclesColor
import com.elian.computeit.core.util.mp_android_chart.toEntries
import com.elian.computeit.databinding.FragmentTestEndBinding
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestEndFragment : Fragment()
{
    private val viewModel by viewModels<TestEndViewModel>()
    private lateinit var binding: FragmentTestEndBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentTestEndBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initUi()
    }

    override fun onStart()
    {
        super.onStart()

        disableNavigateUp()
    }

    private fun initUi()
    {
        binding.apply()
        {
            tvTmp.text = viewModel.getSpeedInTmp().toString()
            tvRawTmp.text = viewModel.getRawSpeedInTpm().toString()
            tvTime.text = "${viewModel.getTestTimeInSeconds()} s"
            tvTests.text = viewModel.getTestCount().toString()
            btnContinue.setOnClickListener { navigate(R.id.action_testEndFragment_to_operationsFragment) }
        }

        initChart()
    }

    private fun initChart()
    {
        val tpmSet = LineDataSet(
            viewModel.getSpeedOverTimeInTpm().toEntries(),
            getString(R.string.generic_tmp)
        ).applyDefaultStyle().apply()
        {
            lineAndCirclesColor = getColorRes(R.color.teal_200)
        }

        val rawTpmSet = LineDataSet(
            viewModel.getRawSpeedOverTimeInTpm().toEntries(),
            getString(R.string.generic_raw)
        ).applyDefaultStyle().apply()
        {
            lineAndCirclesColor = getColorRes(R.color.teal_700)
        }

        binding.lcTestGraph.apply()
        {
            data = LineData().apply()
            {
                addDataSet(rawTpmSet)
                addDataSet(tpmSet)
            }

            animateX(500)

            isDoubleTapToZoomEnabled = false
            setScaleEnabled(false)
            setTouchEnabled(false)
            setDrawGridBackground(false)

            description.isEnabled = false
            axisRight.isEnabled = false

            xAxis.apply()
            {
                setDrawGridLines(false)
                textColor = Color.WHITE
            }
            axisLeft.apply()
            {
                setDrawGridLines(false)
                textColor = Color.WHITE
            }
            legend.apply()
            {
                textColor = Color.WHITE
            }
        }
    }
}
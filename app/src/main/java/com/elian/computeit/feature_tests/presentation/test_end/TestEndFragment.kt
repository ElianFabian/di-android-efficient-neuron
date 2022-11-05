package com.elian.computeit.feature_tests.presentation.test_end

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elian.computeit.R
import com.elian.computeit.core.util.extensions.format
import com.elian.computeit.databinding.FragmentTestEndBinding
import com.github.mikephil.charting.data.Entry
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

    private fun initUi()
    {
        binding.apply()
        {
            val roundedSpeed = viewModel.getAverageSpeed().format("%.2f")
            tvAverageSpeed.text = roundedSpeed
            
            tvTime.text = viewModel.getTestTimeInSeconds().toString()
            tvTests.text = viewModel.getTestCount().toString()
        }

        initChart()
    }

    private fun initChart()
    {
        val entries = viewModel.getSpeedOverTimeInSeconds().map { Entry(it.key.toFloat(), it.value) }

        val lineDataSet = LineDataSet(entries, "Velocity").apply()
        {
            mode = LineDataSet.Mode.CUBIC_BEZIER
            cubicIntensity = 0.2F
            color = getColor(R.color.blue_400)
            setDrawValues(false)
        }

        binding.lcTestGraph.apply()
        {
            data = LineData(lineDataSet)

            animateX(500)

            isDoubleTapToZoomEnabled = false
            setScaleEnabled(false)
            setTouchEnabled(false)
            setBackgroundColor(Color.WHITE)
            setDrawGridBackground(false)
            description.isEnabled = false

            xAxis.apply()
            {
                setDrawGridLines(false)
            }
            axisLeft.apply()
            {
                setDrawGridLines(false)
            }
            axisRight.apply()
            {
                isEnabled = false
                textColor = Color.BLACK;
                axisLineColor = Color.WHITE;
            }
        }
    }
}
package com.elian.computeit.core.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.getColor2
import com.elian.computeit.core.presentation.util.extensions.navigate
import com.elian.computeit.core.presentation.util.mp_android_chart.applyDefaultStyle
import com.elian.computeit.core.presentation.util.mp_android_chart.disableInteraction
import com.elian.computeit.core.presentation.util.mp_android_chart.lineAndCirclesColor
import com.elian.computeit.databinding.FragmentHomeBinding
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment()
{
    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initUi()
    }


    private fun initUi()
    {
        binding.btnGoToConfiguration.setOnClickListener { navigate(R.id.action_homeFragment_to_testConfigurationFragment) }
        
        initChart()
    }

    private fun initChart()
    {
        val tpmSet = LineDataSet(
           listOf(),
            getString(R.string.generic_tmp)
        ).applyDefaultStyle().apply()
        {
            lineAndCirclesColor = getColor2(R.color.teal_200)
        }

        binding.lcAverageTmpInEveryTestSession.applyDefaultStyle().apply()
        {
            data = LineData(tpmSet)

            animateX(500)

            disableInteraction()
        }
    }
}
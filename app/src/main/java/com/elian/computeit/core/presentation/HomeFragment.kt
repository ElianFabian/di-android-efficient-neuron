package com.elian.computeit.core.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.HomeViewModel
import com.elian.computeit.core.presentation.util.extensions.getColorCompat
import com.elian.computeit.core.presentation.util.extensions.navigate
import com.elian.computeit.core.presentation.util.mp_android_chart.applyDefaultStyle
import com.elian.computeit.core.presentation.util.mp_android_chart.lineAndCirclesColor
import com.elian.computeit.core.presentation.util.mp_android_chart.toEntries
import com.elian.computeit.core.util.extensions.tpmPerSession
import com.elian.computeit.databinding.FragmentHomeBinding
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment()
{
    private val viewModel by viewModels<HomeViewModel>()
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

        viewModel

        initUi()
    }


    private fun initUi()
    {
        binding.btnGoToConfiguration.setOnClickListener { navigate(R.id.action_homeFragment_to_testConfigurationFragment) }

        lifecycleScope.launch()
        {
            viewModel.getTestSessionDataList().collect()
            {
                initTpmPerSessionChart(it.tpmPerSession)
            }
        }
    }

    private fun initTpmPerSessionChart(tpmPerSession: List<Int>)
    {
        val tpmSet = LineDataSet(
            tpmPerSession.toEntries(),
            getString(R.string.generic_tmp)
        ).applyDefaultStyle().apply()
        {
            lineAndCirclesColor = getColorCompat(R.color.teal_200)
        }

        binding.lcTpmPerSession.applyDefaultStyle().apply()
        {
            data = LineData(tpmSet)

            animateX(500)
        }
    }
}
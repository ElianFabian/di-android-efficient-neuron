package com.elian.computeit.core.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.domain.models.TestData
import com.elian.computeit.core.presentation.adapters.LabeledDataAdapter
import com.elian.computeit.core.presentation.model.LabeledData
import com.elian.computeit.core.presentation.util.HomeViewModel
import com.elian.computeit.core.presentation.util.extensions.getColorCompat
import com.elian.computeit.core.presentation.util.extensions.getThemeColor
import com.elian.computeit.core.presentation.util.extensions.navigate
import com.elian.computeit.core.presentation.util.mp_android_chart.applyDefault
import com.elian.computeit.core.presentation.util.mp_android_chart.lineDataSet
import com.elian.computeit.core.presentation.util.mp_android_chart.toEntries
import com.elian.computeit.core.util.extensions.*
import com.elian.computeit.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment()
{
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var binding: FragmentHomeBinding

    private lateinit var _testDataListFromServer: List<TestData>


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
        binding.sivGoToTestConfiguration.setOnClickListener { navigate(R.id.action_homeFragment_to_testConfigurationFragment) }
        binding.sivGoToProfile.setOnClickListener { navigate(R.id.action_homeFragment_to_profileFragment) }
        binding.sivGoToTips.setOnClickListener { navigate(R.id.action_homeFragment_to_tipsFragment) }
        binding.sivGoToSettings.setOnClickListener { navigate(R.id.action_homeFragment_to_settingsFragment) }

        if (::_testDataListFromServer.isInitialized)
        {
            initLineChart(_testDataListFromServer)
            initTextInfo(_testDataListFromServer)
        }
        else lifecycleScope.launch()
        {
            binding.lpiIsLoading.isGone = false

            viewModel.getTestDataList().collect()
            {
                _testDataListFromServer = it

                initLineChart(it)
                initTextInfo(it)

                binding.lpiIsLoading.isGone = true
            }
        }
    }

    private fun initLineChart(testDataList: List<TestData>) = testDataList.apply2()
    {
        if (opmPerTest.isNotEmpty() || rawOpmPerTest.isNotEmpty())
        {
            binding.lcOpmPerTest.applyDefault(
                lineDataSet(
                    labelResId = R.string.generic_raw,
                    lineAndCirclesColorResId = R.color.default_chart_25,
                    entries = rawOpmPerTest.toEntries(),
                ) {
                    setDrawVerticalHighlightIndicator(true)
                    highLightColor = getColorCompat(R.color.blue_200)
                },
                lineDataSet(
                    label = getString(R.string.generic_opm),
                    entries = opmPerTest.toEntries(),
                ) {
                    setDrawVerticalHighlightIndicator(true)
                    highLightColor = getColorCompat(R.color.blue_200)
                },
            )
        }
        else binding.lcOpmPerTest.apply()
        {
            setNoDataText(getString(R.string.no_data_available))
            setNoDataTextColor(getThemeColor(R.attr.colorSecondary))
        }

        binding.lcOpmPerTest.isGone = false
    }

    private fun initTextInfo(testDataList: List<TestData>) = testDataList.apply2()
    {
        val list = listOf(
            LabeledData(
                label = getString(R.string.frgHomeFragment_testsCompleted),
                value = "$testsCompleted",
            ),
            LabeledData(
                label = getString(R.string.frgHomeFragment_operationsCompleted),
                value = "$operationsCompleted",
            ),
            LabeledData(
                label = getString(R.string.frgHomeFragment_correctOperationsCompleted),
                value = "$correctOperationsCompleted (${correctOperationsCompletedPercentage.toInt()} %)",
            ),
            LabeledData(
                label = getString(R.string.frgHomeFragment_averageOpm),
                value = averageOpm.defaultFormat(),
            ),
            LabeledData(
                label = getString(R.string.frgHomeFragment_averageRawOpm),
                value = averageRawOpm.defaultFormat(),
            ),
            LabeledData(
                label = getString(R.string.frgHomeFragment_highestOpm),
                value = "$maxOpm",
            ),
            LabeledData(
                label = getString(R.string.frgHomeFragment_highestRawOpm),
                value = "$maxRawOpm",
            ),
        )

        val adapter = LabeledDataAdapter()
        binding.rvLabeledData.adapter = adapter

        lifecycleScope.launch()
        {
            val itemAdditionDelay = 175L
            val labeledDataList = mutableListOf<LabeledData>()

            list.forEach()
            {
                delay(itemAdditionDelay)
                adapter.submitList(labeledDataList.apply { add(it) }.toList())
            }
        }
    }
}
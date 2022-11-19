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
import com.elian.computeit.core.presentation.util.HomeViewModel
import com.elian.computeit.core.presentation.util.extensions.getColorCompat
import com.elian.computeit.core.presentation.util.extensions.getThemeColor
import com.elian.computeit.core.presentation.util.extensions.navigate
import com.elian.computeit.core.presentation.util.extensions.text2
import com.elian.computeit.core.presentation.util.mp_android_chart.applyDefault
import com.elian.computeit.core.presentation.util.mp_android_chart.lineDataSet
import com.elian.computeit.core.presentation.util.mp_android_chart.toEntries
import com.elian.computeit.core.util.extensions.*
import com.elian.computeit.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
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
        binding.btnGoToConfiguration.setOnClickListener { navigate(R.id.action_homeFragment_to_testConfigurationFragment) }

        if (::_testDataListFromServer.isInitialized)
        {
            initLineChart(_testDataListFromServer)
            initTextInfo(_testDataListFromServer)
        }
        else lifecycleScope.launch()
        {
            binding.lpiIsLoding.isGone = false

            viewModel.getTestDataList().collect()
            {
                _testDataListFromServer = it

                initLineChart(it)
                initTextInfo(it)

                binding.lpiIsLoding.isGone = true
            }
        }
    }

    private fun initTextInfo(testDataList: List<TestData>) = binding.apply2()
    {
        testDataList.apply()
        {
            tvTestsCompleted.text2 = "$size"
            tvOperationsCompleted.text2 = "$operationsCompleted"
            tvCorrectOperationsCompleted.text2 = "$correctOperationsCompleted (${correctOperationsCompletedPercentage.toInt()} %)"
            tvAverageOpm.text2 = averageOpm.defaultFormat()
            tvAverageRawOpm.text2 = averageRawOpm.defaultFormat()
            tvHighestOpm.text2 = "$maxOpm"
            tvHighestRawOpm.text2 = "$maxRawOpm"
        }
    }

    private fun initLineChart(testDataList: List<TestData>) = testDataList.apply2()
    {
        if (opmPerTest.isNotEmpty() || opmPerTest.isNotEmpty())
        {
            binding.lcOpmPerTest.applyDefault(
                lineDataSet(
                    labelResId = R.string.generic_raw,
                    lineAndCirclesColorResId = R.color.default_chart_25,
                    entries = rawOpmPerTest.toEntries(),
                ),
                lineDataSet(
                    label = getString(R.string.generic_opm),
                    entries = opmPerTest.toEntries(),
                ) {
                    setDrawVerticalHighlightIndicator(true)
                    highLightColor = context.getColorCompat(R.color.blue_200)
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
}
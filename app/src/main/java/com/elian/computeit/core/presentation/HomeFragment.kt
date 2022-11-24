package com.elian.computeit.core.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elian.computeit.R
import com.elian.computeit.core.presentation.adapters.LabeledDataAdapter
import com.elian.computeit.core.presentation.model.LabeledData
import com.elian.computeit.core.presentation.util.HomeViewModel
import com.elian.computeit.core.presentation.util.extensions.*
import com.elian.computeit.core.presentation.util.mp_android_chart.applyDefault
import com.elian.computeit.core.presentation.util.mp_android_chart.lineDataSet
import com.elian.computeit.core.presentation.util.mp_android_chart.toEntries
import com.elian.computeit.core.util.constants.DEFAULT_DECIMAL_FORMAT
import com.elian.computeit.core.util.extensions.apply2
import com.elian.computeit.core.util.extensions.format
import com.elian.computeit.databinding.FragmentHomeBinding
import com.elian.computeit.feature_tests.domain.models.TestListInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class HomeFragment : Fragment()
{
    private var _isUiFinished = false
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

        subscribeToEvents()
        initUi()
    }


    private fun initUi()
    {
        binding.sivGoToTestConfiguration.setOnClickListener { navigate(R.id.action_homeFragment_to_testConfigurationFragment) }
        binding.sivGoToProfile.setOnClickListener { navigate(R.id.action_homeFragment_to_profileFragment) }
//        binding.sivGoToTips.setOnClickListener { navigate(R.id.action_homeFragment_to_tipsFragment) }
//        binding.sivGoToSettings.setOnClickListener { navigate(R.id.action_homeFragment_to_settingsFragment) }
    }

    private fun subscribeToEvents() = viewModel.apply2()
    {
        collectLatestFlowWhenStarted(infoState.filterNotNull())
        {
            initLineChart(it)
            initTextInfo(it)
        }
        collectFlowWhenStarted(isLoadingState) { binding.lpiIsLoading.isGone = !it }
    }

    private fun initLineChart(info: TestListInfo) = info.apply2()
    {
        if (opmPerTest.isNotEmpty() || rawOpmPerTest.isNotEmpty())
        {
            val lineDataSets = arrayOf(
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

            binding.lcOpmPerTest.applyDefault(
                animate = !_isUiFinished,
                dataSets = lineDataSets,
            )
        }
        else binding.lcOpmPerTest.apply()
        {
            setNoDataText(getString(R.string.no_data_available))
            setNoDataTextColor(getThemeColor(R.attr.colorSecondary))
        }

        binding.lcOpmPerTest.isVisible = true
        _isUiFinished = true
    }

    private fun initTextInfo(info: TestListInfo) = info.apply2()
    {
        val uiLabeledDataList = listOf(
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
                value = averageOpm.format(DEFAULT_DECIMAL_FORMAT),
            ),
            LabeledData(
                label = getString(R.string.frgHomeFragment_averageRawOpm),
                value = averageRawOpm.format(DEFAULT_DECIMAL_FORMAT),
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
        adapter.submitList(uiLabeledDataList)
    }
}
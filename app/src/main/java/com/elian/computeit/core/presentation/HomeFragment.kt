package com.elian.computeit.core.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elian.computeit.R
import com.elian.computeit.core.presentation.adapter.MainLabeledDataAdapter
import com.elian.computeit.core.presentation.adapter.TestInfoMarker
import com.elian.computeit.core.presentation.model.labelOf
import com.elian.computeit.core.presentation.util.extensions.avoidConflictsWithScroll
import com.elian.computeit.core.presentation.util.extensions.collectLatestFlowWhenStarted
import com.elian.computeit.core.presentation.util.extensions.getColorCompat
import com.elian.computeit.core.presentation.util.extensions.navigate
import com.elian.computeit.core.presentation.util.mp_android_chart.*
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.constants.toBundle
import com.elian.computeit.core.util.using
import com.elian.computeit.databinding.FragmentHomeBinding
import com.elian.computeit.feature_tests.domain.args.TestDetailsArgs
import com.elian.computeit.feature_tests.domain.model.TestHistoryInfo
import com.elian.computeit.feature_tests.domain.model.TestInfo
import com.elian.computeit.feature_tests.domain.model.TestListInfo
import com.elian.computeit.feature_tests.domain.model.TestListStatsInfo
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home)
{
	private val viewModel by viewModels<HomeViewModel>()
	private val binding by viewBinding(FragmentHomeBinding::bind)

	private val rangeFormatter = RangeValueFormatter()


	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		subscribeToEvents()
		initUi()
	}


	private fun initUi()
	{
		binding.sivGoToTestConfiguration.setOnClickListener { navigate(R.id.action_homeFragment_to_testConfigurationFragment) }
		binding.sivGoToProfile.setOnClickListener { navigate(R.id.action_homeFragment_to_privateProfileFragment) }
//        binding.sivGoToTips.setOnClickListener { navigate(R.id.action_homeFragment_to_tipsFragment) }
//        binding.sivGoToSettings.setOnClickListener { navigate(R.id.action_homeFragment_to_settingsFragment) }

		binding.viewTestHistory.lineChart.isGone = true
		binding.bcSpeedHistogram.isGone = true
		binding.sldRangeLength.isGone = true
	}

	private fun subscribeToEvents() = using(viewModel)
	{
		collectLatestFlowWhenStarted(state)
		{
			it.info?.also { info ->

				initTestHistoryChart(info.historyInfo)
				initSpeedHistogramChart(info)
				initTextInfo(info.statsInfo)
			}

			binding.lpiIsLoading.isVisible = it.isLoading
		}
	}

	private fun initTestHistoryChart(info: TestHistoryInfo) = using(info)
	{
		val chartView = binding.viewTestHistory.lineChart

		if (opmPerTest.isNotEmpty() || rawOpmPerTest.isNotEmpty())
		{
			val lineDataSets = arrayOf(
				lineDataSet(
					labelResId = R.string.generic_raw,
					lineAndCirclesColorResId = R.color.chart_secondary,
					entries = rawOpmPerTest.toEntries(listOfData = listOfTestInfo),
				) {
					setDrawVerticalHighlightIndicator(true)
					highLightColor = getColorCompat(R.color.blue_200)
				},
				lineDataSet(
					label = getString(R.string.generic_opm),
					entries = opmPerTest.toEntries(listOfData = listOfTestInfo),
				) {
					setDrawVerticalHighlightIndicator(true)
					highLightColor = getColorCompat(R.color.blue_200)
				},
			)

			chartView.applyDefault(
				dataSets = lineDataSets,
			)

			chartView.marker2 = TestInfoMarker(context)
			chartView.avoidConflictsWithScroll(binding.root)
			chartView.setOnChartValueSelectedListener(object : OnChartValueSelectedListener
			{
				var currentSelectedEntry: Entry? = null

				override fun onValueSelected(entry: Entry, highlight: Highlight)
				{
					if (currentSelectedEntry == entry)
					{
						navigate(R.id.action_homeFragment_to_testDetailsFragment,
							TestDetailsArgs(
								testInfo = entry.data as TestInfo,
							).toBundle()
						)
					}

					currentSelectedEntry = entry
				}

				override fun onNothingSelected()
				{
				}
			})
		}
		else chartView.showNoDataText()

		chartView.isVisible = true
	}

	private fun initSpeedHistogramChart(info: TestListInfo) = using(info)
	{
		val chartView = binding.bcSpeedHistogram

		if (speedHistogramInfo.testsPerSpeedRange.isNotEmpty())
		{
			chartView.apply()
			{
				avoidConflictsWithScroll(binding.root)
				applyDefault()

				xAxis.valueFormatter = rangeFormatter.apply()
				{
					rangeLength = speedHistogramInfo.speedRangeLength
					minOpm = statsInfo.minOpm.toFloat()
					maxOpm = statsInfo.maxOpm.toFloat()
				}

				data = BarData(
					barDataSet(
						entries = speedHistogramInfo.testsPerSpeedRange.toBarEntries(),
						labelResId = R.string.generic_tests,
					),
				)
			}

			binding.sldRangeLength.apply()
			{
				speedHistogramInfo.also()
				{
					valueFrom = it.speedRangeLengthMinValue.toFloat()
					valueTo = it.speedRangeLengthMaxValue.toFloat()
					value = it.speedRangeLength.toFloat()
					isVisible = it.isSliderVisible
				}

				addOnChangeListener { _, value, _ ->

					val newTestsPerSpeedRange = viewModel.getTestsPerSpeedRange(rangeLength = value.toInt())

					rangeFormatter.rangeLength = value.toInt()

					chartView.data = BarData(
						barDataSet(
							entries = newTestsPerSpeedRange.toBarEntries(),
							labelResId = R.string.generic_tests,
						),
					)
					chartView.invalidate()
				}
			}
		}
		else chartView.showNoDataText()

		chartView.isVisible = true
	}

	private fun initTextInfo(info: TestListStatsInfo) = using(info)
	{
		val listOfLabeledData = listOf(
			R.string.frgHome_testsCompleted labelOf testsCompleted,
			R.string.generic_totalTime labelOf totalTime,
			R.string.frgHome_operationsCompleted labelOf operationsCompleted,
			R.string.frgHome_correctOperationsCompleted labelOf "$correctOperationsCompleted (${correctOperationsCompletedPercentage.toInt()} %)",
			R.string.frgHome_averageOpm labelOf averageOpm.toInt(),
			R.string.frgHome_averageRawOpm labelOf averageRawOpm.toInt(),
			R.string.frgHome_highestOpm labelOf maxOpm,
			R.string.frgHome_highestRawOpm labelOf maxRawOpm,
		)

		binding.viewTextInfoList.rvLabeledData.adapter = MainLabeledDataAdapter(listOfLabeledData)
	}
}
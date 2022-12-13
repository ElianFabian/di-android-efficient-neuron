package com.elian.computeit.core.presentation

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elian.computeit.R
import com.elian.computeit.core.presentation.adapter.MainLabeledDataAdapter
import com.elian.computeit.core.presentation.adapter.TestInfoMarker
import com.elian.computeit.core.presentation.model.labelOf
import com.elian.computeit.core.presentation.util.extensions.*
import com.elian.computeit.core.presentation.util.mp_android_chart.*
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.constants.TestDetailsArgKeys
import com.elian.computeit.core.util.extensions.apply2
import com.elian.computeit.databinding.FragmentHomeBinding
import com.elian.computeit.feature_tests.domain.model.TestHistoryInfo
import com.elian.computeit.feature_tests.domain.model.TestInfo
import com.elian.computeit.feature_tests.domain.model.TestListInfo
import com.elian.computeit.feature_tests.domain.model.TestListStatsInfo
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home)
{
	private val viewModel by viewModels<HomeViewModel>()
	private val binding by viewBinding(FragmentHomeBinding::bind)

	private var _isUiFinished = false

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

		binding.lytTestHistory.lineChart.isGone = true
		binding.bcSpeedHistogram.isGone = true
		binding.sldRangeLength.isGone = true
	}

	private fun subscribeToEvents() = viewModel.apply2()
	{
		collectLatestFlowWhenStarted(infoState.filterNotNull())
		{
			initTestHistoryChart(it.historyInfo)
			initSpeedHistogramChart(it)
			initTextInfo(it.statsInfo)
		}
		collectFlowWhenStarted(isLoadingState) { binding.lpiIsLoading.isGone = !it }
	}

	private fun initTestHistoryChart(info: TestHistoryInfo) = info.apply2()
	{
		val chartView = binding.lytTestHistory.lineChart

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
				animate = !_isUiFinished,
				dataSets = lineDataSets,
			)
		}
		else chartView.apply()
		{
			setNoDataText(getString(R.string.no_data_available))
			setNoDataTextColor(getThemeColor(R.attr.colorSecondary))
		}

		chartView.isVisible = true
		chartView.marker2 = TestInfoMarker(context)
		chartView.avoidConflictsWithScroll(binding.root)
		chartView.setOnChartValueSelectedListener(object : OnChartValueSelectedListener
		{
			var currentSelectedEntry: Entry? = null

			override fun onValueSelected(entry: Entry, highlight: Highlight)
			{
				if (currentSelectedEntry == entry)
				{
					navigate(R.id.action_homeFragment_to_testDetailsFragment, bundleOf(
						TestDetailsArgKeys.TestInfo to entry.data as TestInfo,
						TestDetailsArgKeys.HideContinueButton to true,
					))
				}

				currentSelectedEntry = entry
			}

			override fun onNothingSelected()
			{
			}
		})

		_isUiFinished = true
	}

	private fun initSpeedHistogramChart(info: TestListInfo) = info.apply2()
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
					maxLength = statsInfo.maxOpm
				}

				data = BarData(
					BarDataSet(
						speedHistogramInfo.testsPerSpeedRange.toBarEntries(),
						getString(R.string.generic_tests),
					).apply { color = getColorCompat(R.color.teal_200) }
				)
			}

			binding.sldRangeLength.apply()
			{
				valueTo = statsInfo.maxOpm.toFloat()
				binding.sldRangeLength.value = speedHistogramInfo.speedRangeLength.toFloat()

				addOnChangeListener { _, value, _ ->

					val newInfo = viewModel.getSpeedHistogram(value.toInt())

					rangeFormatter.rangeLength = value.toInt()

					chartView.data = BarData(
						BarDataSet(
							newInfo.testsPerSpeedRange.toBarEntries(),
							getString(R.string.generic_tests),
						).apply { color = getColorCompat(R.color.teal_200) }
					)
					chartView.invalidate()
				}

				isVisible = true
			}
		}
		else chartView.apply()
		{
			setNoDataText(getString(R.string.no_data_available))
			setNoDataTextColor(getThemeColor(R.attr.colorSecondary))
		}

		chartView.isVisible = true
	}

	private fun initTextInfo(info: TestListStatsInfo) = info.apply2()
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

		binding.lytTextInfoList.rvLabeledData.adapter = MainLabeledDataAdapter(listOfLabeledData)
	}
}
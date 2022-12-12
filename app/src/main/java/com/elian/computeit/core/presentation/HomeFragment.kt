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
import com.elian.computeit.core.presentation.model.withLabel
import com.elian.computeit.core.presentation.util.extensions.*
import com.elian.computeit.core.presentation.util.mp_android_chart.*
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.constants.TestDetailsArgKeys
import com.elian.computeit.core.util.extensions.apply2
import com.elian.computeit.databinding.FragmentHomeBinding
import com.elian.computeit.feature_tests.domain.model.SpeedHistogramInfo
import com.elian.computeit.feature_tests.domain.model.TestHistoryInfo
import com.elian.computeit.feature_tests.domain.model.TestInfo
import com.elian.computeit.feature_tests.domain.model.TestListStatsInfo
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
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
	}

	private fun subscribeToEvents() = viewModel.apply2()
	{
		collectLatestFlowWhenStarted(infoState.filterNotNull())
		{
			initTestHistoryChart(it.historyInfo)
			initSpeedHistogramChart(it.speedHistogramInfo)
			initTextInfo(it.statsInfo)
		}
		collectFlowWhenStarted(isLoadingState) { binding.lpiIsLoading.isGone = !it }
	}

	private fun initTestHistoryChart(info: TestHistoryInfo) = info.apply2()
	{
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

			binding.lytTestHistory.lineChart.applyDefault(
				animate = !_isUiFinished,
				dataSets = lineDataSets,
			)
		}
		else binding.lytTestHistory.lineChart.apply()
		{
			setNoDataText(getString(R.string.no_data_available))
			setNoDataTextColor(getThemeColor(R.attr.colorSecondary))
		}

		binding.lytTestHistory.lineChart.isVisible = true

		binding.lytTestHistory.lineChart.marker2 = TestInfoMarker(context)

		_isUiFinished = true

		binding.lytTestHistory.lineChart.avoidConflictsWithScroll(binding.root)

		binding.lytTestHistory.lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener
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
	}

	private fun initSpeedHistogramChart(info: SpeedHistogramInfo) = info.apply2()
	{
		if (info.testsPerSpeedRange.isNotEmpty())
		{
			binding.bcSpeedHistogram.avoidConflictsWithScroll(binding.root)
			binding.bcSpeedHistogram.applyDefault()

			binding.bcSpeedHistogram.xAxis.valueFormatter = object : ValueFormatter()
			{
				override fun getFormattedValue(value: Float): String
				{
					val valueToInt = value.toInt()

					val start = valueToInt * info.speedRangeLength
					val end = (valueToInt + 1) * info.speedRangeLength - 1

					return "$start−$end"
				}
			}

			binding.bcSpeedHistogram.data = BarData(
				BarDataSet(
					info.testsPerSpeedRange.toBarEntries(),
					getString(R.string.generic_tests),
				).apply { color = getColorCompat(R.color.teal_200) }
			)
		}
		else binding.bcSpeedHistogram.apply()
		{
			setNoDataText(getString(R.string.no_data_available))
			setNoDataTextColor(getThemeColor(R.attr.colorSecondary))
		}

		binding.bcSpeedHistogram.isVisible = true
	}

	private fun initTextInfo(info: TestListStatsInfo) = info.apply2()
	{
		val listOfUiLabeledData = listOf(
			testsCompleted withLabel R.string.frgHome_testsCompleted,
			totalTime withLabel R.string.generic_totalTime,
			operationsCompleted withLabel R.string.frgHome_operationsCompleted,
			"$correctOperationsCompleted (${correctOperationsCompletedPercentage.toInt()} %)" withLabel R.string.frgHome_correctOperationsCompleted,
			averageOpm.toInt() withLabel R.string.frgHome_averageOpm,
			averageRawOpm.toInt() withLabel R.string.frgHome_averageRawOpm,
			maxOpm withLabel R.string.frgHome_highestOpm,
			maxRawOpm withLabel R.string.frgHome_highestRawOpm,
		)

		binding.lytTextInfoList.rvLabeledData.adapter = MainLabeledDataAdapter(listOfUiLabeledData)
	}
}
package com.elian.computeit.core.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.elian.computeit.R
import com.elian.computeit.core.domain.models.TestHistoryInfo
import com.elian.computeit.core.domain.models.TestListInfo
import com.elian.computeit.core.domain.models.TestListStatsInfo
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
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home)
{
	private val viewModel by activityViewModels<HomeViewModel>()
	private val binding by viewBinding(FragmentHomeBinding::bind)

	private val _rangeFormatter = RangeValueFormatter()


	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		subscribeToEvents()
		initializeUi()
	}


	private fun initializeUi()
	{
		binding.sivGoToTestConfiguration.setOnClickListener { navigate(R.id.action_homeFragment_to_testConfigurationFragment) }
		binding.sivGoToProfile.setOnClickListener { navigate(R.id.action_homeFragment_to_privateProfileFragment) }

		viewModel.fetchInfo()
	}

	private fun subscribeToEvents() = using(viewModel)
	{
		collectLatestFlowWhenStarted(state)
		{
			it.info?.also { info ->

				initializeTestHistoryChart(info.historyInfo)
				initializeSpeedHistogramChart(info)
				initializeTextStats(info.statsInfo)
			}

			binding.apply()
			{
				lpiIsLoading.isVisible = it.isLoading

				lcTestsHistory.isGone = it.isLoading
				bcSpeedHistogram.isGone = it.isLoading
				sldRangeLength.isGone = it.isLoading
			}
		}
	}

	private fun initializeTestHistoryChart(info: TestHistoryInfo) = using(info)
	{
		val chartView = binding.lcTestsHistory

		if (listOfOpmPerTest.isNotEmpty() || listOfRawOpmPerTest.isNotEmpty())
		{
			val lineDataSets = arrayOf(
				lineDataSet(
					labelResId = R.string.generic_raw,
					lineAndCirclesColorResId = R.color.chart_secondary,
					entries = listOfRawOpmPerTest.toEntries(startXValue = 1),
				) {
					setDrawVerticalHighlightIndicator(true)
					highLightColor = getColorCompat(R.color.blue_200)
				},
				lineDataSet(
					label = getString(R.string.generic_opm),
					entries = listOfOpmPerTest.toEntries(startXValue = 1),
				) {
					setDrawVerticalHighlightIndicator(true)
					highLightColor = getColorCompat(R.color.blue_200)
				},
			)

			chartView.applyDefault(dataSets = lineDataSets)
			chartView.invalidate()

			chartView.marker2 = TestInfoMarker(
				context = context,
				items = info.listOfTestInfo,
			)
			chartView.avoidConflictsWithScroll(binding.root)
			chartView.setOnChartValueSelectedListener(object : OnChartValueSelectedListener
			{
				private val btnGoToTestDetails = binding.btnGoToTestDetails

				override fun onValueSelected(entry: Entry, highlight: Highlight)
				{
					val position = entry.x.toInt() - 1
					val selectedTestInfo = info.listOfTestInfo[position]

					btnGoToTestDetails.setOnClickListener()
					{
						navigate(R.id.action_homeFragment_to_testDetailsFragment,
							TestDetailsArgs(
								testInfo = selectedTestInfo,
							).toBundle()
						)
					}
					btnGoToTestDetails.text = "${getString(R.string.frgHome_go_to_test)} ${entry.x.toInt()}"
					btnGoToTestDetails.isEnabled = true
				}

				override fun onNothingSelected()
				{
					btnGoToTestDetails.setOnClickListener(null)
					btnGoToTestDetails.isEnabled = false
					btnGoToTestDetails.setText(R.string.frgHome_go_to_test)
				}
			})

			binding.btnGoToTestDetails.isVisible = true
			binding.btnGoToTestDetails.setText(R.string.frgHome_go_to_test)
		}
		else chartView.showNoDataText()
	}

	private fun initializeSpeedHistogramChart(info: TestListInfo) = using(info)
	{
		val chartView = binding.bcSpeedHistogram

		if (speedHistogramInfo.testsPerSpeedRange.isNotEmpty())
		{
			chartView.applyDefault()
			{
				avoidConflictsWithScroll(binding.root)

				xAxis.valueFormatter = _rangeFormatter.apply()
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
					valueFrom = it.speedRangeLengthValueFrom.toFloat()
					valueTo = it.speedRangeLengthValueTo.toFloat()
					value = it.speedRangeLength.toFloat()
					isVisible = it.isSliderVisible
				}

				addOnChangeListener { _, value, _ ->

					val newListOfTestsPerSpeedRange = viewModel.getListOfTestsPerSpeedRange(rangeLength = value.toInt())

					_rangeFormatter.rangeLength = value.toInt()

					chartView.data = BarData(
						barDataSet(
							entries = newListOfTestsPerSpeedRange.toBarEntries(),
							labelResId = R.string.generic_tests,
						),
					)
					chartView.invalidate()
				}
			}
		}
		else chartView.showNoDataText()
	}

	private fun initializeTextStats(info: TestListStatsInfo) = using(info)
	{
		val listOfLabeledData = listOf(
			R.string.frgHome_testsCompleted labelOf testsCompletedCount,
			R.string.frgHome_testsCompletedWithoutErrors labelOf "$testsCompletedWithoutErrorsCount (${testsCompletedWithoutErrorsPercentage.toInt()} %)",
			R.string.generic_totalTime labelOf formattedTotalTime,
			R.string.frgHome_operationsCompleted labelOf operationsCompleted,
			R.string.frgHome_correctOperationsCompleted labelOf "$correctOperationsCompletedCount (${correctOperationsCompletedPercentage.toInt()} %)",
			R.string.frgHome_averageOpm labelOf averageOpm.toInt(),
			R.string.frgHome_averageRawOpm labelOf averageRawOpm.toInt(),
			R.string.frgHome_highestOpm labelOf maxOpm,
			R.string.frgHome_highestRawOpm labelOf maxRawOpm,
		)

		binding.viewTestListStats.rvLabeledData.adapter = MainLabeledDataAdapter(listOfLabeledData)
	}
}
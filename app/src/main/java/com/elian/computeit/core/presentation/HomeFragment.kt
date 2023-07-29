package com.elian.computeit.core.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.elian.computeit.R
import com.elian.computeit.core.domain.models.TestListInfo
import com.elian.computeit.core.presentation.adapter.MainLabeledDataAdapter
import com.elian.computeit.core.presentation.adapter.TestInfoMarker
import com.elian.computeit.core.presentation.model.labelOf
import com.elian.computeit.core.presentation.util.extensions.avoidConflictsWithScroll
import com.elian.computeit.core.presentation.util.extensions.getColorCompat
import com.elian.computeit.core.presentation.util.extensions.onClick
import com.elian.computeit.core.presentation.util.extensions.onScrollChanged
import com.elian.computeit.core.presentation.util.extensions.onValueChanged
import com.elian.computeit.core.presentation.util.mp_android_chart.RangeValueFormatter
import com.elian.computeit.core.presentation.util.mp_android_chart.applyDefault
import com.elian.computeit.core.presentation.util.mp_android_chart.barDataSet
import com.elian.computeit.core.presentation.util.mp_android_chart.lineDataSet
import com.elian.computeit.core.presentation.util.mp_android_chart.marker2
import com.elian.computeit.core.presentation.util.mp_android_chart.setOnChartValueSelectedListener
import com.elian.computeit.core.presentation.util.mp_android_chart.showNoDataText
import com.elian.computeit.core.presentation.util.mp_android_chart.toBarEntries
import com.elian.computeit.core.presentation.util.mp_android_chart.toEntries
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.constants.toBundle
import com.elian.computeit.databinding.FragmentHomeBinding
import com.github.mikephil.charting.data.BarData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

	private val viewModel by viewModels<HomeViewModel>()
	private val binding by viewBinding(FragmentHomeBinding::bind)
	private val navController by lazy { findNavController() }

	private val _rangeFormatter = RangeValueFormatter()


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		subscribeToEvents()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initUi()
	}


	private fun initUi() {
		binding.apply {
			sivGoToTestConfiguration.onClick {
				navController.navigate(R.id.action_homeFragment_to_testConfigurationFragment)
			}
			sivGoToProfile.onClick {
				navController.navigate(R.id.action_homeFragment_to_privateProfileFragment)
			}

			btnGoToTestDetails.onClick {
				viewModel.onAction(HomeAction.GoToTestDetail)
			}

			sldRangeLength.onValueChanged { value, isFromUser ->
				if (!isFromUser) return@onValueChanged

				viewModel.onAction(HomeAction.ChangeRangeLength(value.toInt()))
			}

			bcSpeedHistogram.xAxis.valueFormatter = _rangeFormatter

			svRoot.onScrollChanged { _, scrollY, _, _ ->
				viewModel.onAction(HomeAction.ChangeVerticalScroll(scrollY))
			}

			// I didn't found a way to properly get the scale and position of the chart
//			binding.lcTestsHistory.setOnChartGestureListener(
//				onChartScale = { me, scaleX, scaleY ->
//					
//				},
//				onChartTranslate = { _, _, _ ->
//					
//				}
//			)
		}
	}

	private fun subscribeToEvents() {
		lifecycleScope.launch {
			viewModel.state.flowWithLifecycle(lifecycle)
				.collectLatest { state ->

					val speedRangeLength = state.speedRangeLength

					if (speedRangeLength != null) {
						_rangeFormatter.apply {
							rangeLength = speedRangeLength
						}
						binding.sldRangeLength.value = speedRangeLength.toFloat()
					}

					binding.apply {
						lpiIsLoading.isVisible = state.isLoading
						sldRangeLength.isGone = state.isLoading
					}
				}
		}
		lifecycleScope.launch {
			viewModel.screenScrollY.flowWithLifecycle(lifecycle)
				.collectLatest { scrollY ->
					binding.svRoot.scrollY = scrollY
				}
		}
		lifecycleScope.launch {
			viewModel.testCountPerSpeedRange.flowWithLifecycle(lifecycle)
				.collectLatest { testCountPerSpeedRange ->
					binding.bcSpeedHistogram.apply {
						data = BarData(
							barDataSet(
								entries = testCountPerSpeedRange.toBarEntries(),
								labelResId = R.string.Tests,
							),
						)
						invalidate()
					}
				}
		}
		lifecycleScope.launch {
			viewModel.info.flowWithLifecycle(lifecycle)
				.collectLatest { info ->
					initializeTestHistoryChart(info)
					initializeSpeedHistogramChart(info)
					initializeTextStats(info)

					info?.also {
						_rangeFormatter.apply {
							minOpm = info.minOpm
							maxOpm = info.maxOpm
						}

						binding.sldRangeLength.apply {
							valueFrom = info.speedRangeLengthValueFrom.toFloat()
							valueTo = info.speedRangeLengthValueTo.toFloat()
						}
					}
				}
		}
		lifecycleScope.launch {
			viewModel.state.flowWithLifecycle(lifecycle)
				.collectLatest { state ->
					val selectedTestIndex = state.selectedTestIndex
					val isThereASelectedTest = selectedTestIndex != -1

					if (isThereASelectedTest) {
						binding.lcTestsHistory.highlightValue(selectedTestIndex + 1F, 1)
					}
					binding.btnGoToTestDetails.apply {
						isEnabled = isThereASelectedTest
						text = buildString {
							append(getString(R.string.GoToTest))

							if (isThereASelectedTest) {
								append(" ${selectedTestIndex + 1}")
							}
						}
					}
				}
		}
		lifecycleScope.launch {
			viewModel.eventFlow.flowWithLifecycle(lifecycle)
				.collect { event ->
					when (event) {
						is HomeEvent.OnGoToTestDetail -> {
							navController.navigate(
								resId = R.id.action_homeFragment_to_testDetailsFragment,
								args = event.args.toBundle(),
							)
						}
					}
				}
		}
	}

	private fun initializeTestHistoryChart(info: TestListInfo?) {
		val chartView = binding.lcTestsHistory

		binding.btnGoToTestDetails.isInvisible = info == null
		chartView.isInvisible = info == null

		if (info == null) return

		if (info.listOfOpmPerTest.isNotEmpty() || info.listOfRawOpmPerTest.isNotEmpty()) {
			val lineDataSets = arrayOf(
				lineDataSet(
					labelResId = R.string.Raw,
					lineAndCirclesColorResId = R.color.chart_secondary,
					entries = info.listOfRawOpmPerTest.toEntries(startXValue = 1),
				) {
					setDrawVerticalHighlightIndicator(true)
					highLightColor = getColorCompat(R.color.blue_200)
				},
				lineDataSet(
					label = getString(R.string.OPM__OperationsPerMinute),
					entries = info.listOfOpmPerTest.toEntries(startXValue = 1),
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
			chartView.setOnChartValueSelectedListener(
				onValueSelected = { entry, _ ->
					val index = entry.x.toInt() - 1

					viewModel.onAction(HomeAction.SelectTest(index))
				},
				onNothingSelected = {
					viewModel.onAction(HomeAction.UnSelectTest)
				},
			)
		}
		else chartView.showNoDataText()
	}

	private fun initializeSpeedHistogramChart(info: TestListInfo?) {
		val chartView = binding.bcSpeedHistogram

		chartView.isInvisible = info == null

		if (info == null) return

		if (info.testsPerSpeedRange.isNotEmpty()) {
			chartView.applyDefault {
				avoidConflictsWithScroll(binding.root)
			}
		}
		else chartView.showNoDataText()
	}

	private fun initializeTextStats(info: TestListInfo?) {

		if (info == null) return

		info.apply {
			val listOfLabeledData = listOf(
				R.string.TestsCompleted labelOf testsCompletedCount,
				R.string.TestsCompletedWithoutErrors labelOf "$testsCompletedWithoutErrorsCount (${testsCompletedWithoutErrorsPercentage.toInt()} %)",
				R.string.TotalTime labelOf formattedTotalTime,
				R.string.OperationsCompleted labelOf operationsCompleted,
				R.string.CorrectOperationsCompleted labelOf "$correctOperationsCompletedCount (${correctOperationsCompletedPercentage.toInt()} %)",
				R.string.AverageOPM labelOf averageOpm.toInt(),
				R.string.AverageRawOPM labelOf averageRawOpm.toInt(),
				R.string.HighestOPM labelOf maxOpm.toInt(),
				R.string.HighestRawOPM labelOf maxRawOpm.toInt(),
			)

			binding.viewTestListStats.rvLabeledData.adapter = MainLabeledDataAdapter(listOfLabeledData)
		}
	}
}
package com.elian.computeit.feature_tests.presentation.test_details

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.elian.computeit.R
import com.elian.computeit.core.presentation.adapter.MainLabeledDataAdapter
import com.elian.computeit.core.presentation.model.labelOf
import com.elian.computeit.core.presentation.util.extensions.avoidConflictsWithScroll
import com.elian.computeit.core.presentation.util.extensions.navigate
import com.elian.computeit.core.presentation.util.mp_android_chart.applyDefault
import com.elian.computeit.core.presentation.util.mp_android_chart.lineDataSet
import com.elian.computeit.core.presentation.util.mp_android_chart.toEntries
import com.elian.computeit.core.presentation.util.mp_android_chart.valuesToEntriesWithYValue
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.constants.TestDetailsArgKeys
import com.elian.computeit.core.util.using
import com.elian.computeit.databinding.FragmentTestDetailsBinding
import com.elian.computeit.feature_tests.domain.model.TestChartInfo
import com.elian.computeit.feature_tests.domain.model.TestInfo
import com.elian.computeit.feature_tests.domain.model.TestStatsInfo
import com.elian.computeit.feature_tests.presentation.test_details.adapter.FailedOperationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestDetailsFragment : Fragment(R.layout.fragment_test_details)
{
	private val binding by viewBinding(FragmentTestDetailsBinding::bind)


	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		initUi()
	}


	private fun initUi() = using(binding)
	{
		val info = arguments?.getParcelable<TestInfo>(TestDetailsArgKeys.TestInfo)!!

		initTestChart(info.chartInfo)
		initStats(info.statsInfo)

		if (info.listOfFailedOperationInfo.isEmpty())
		{
			tvFailedOperations.isGone = true
			rvFailedOperations.isGone = true
		}
		else initFailedOperationsAdapter(info)

		btnContinue.setOnClickListener { navigate(R.id.action_testDetailsFragment_to_homeFragment) }
		btnContinue.isGone = arguments?.getBoolean(TestDetailsArgKeys.HideContinueButton) ?: false
	}

	private fun initTestChart(info: TestChartInfo)
	{
		val lineDataSets = arrayOf(
			lineDataSet(
				labelResId = R.string.generic_raw,
				lineAndCirclesColorResId = R.color.chart_secondary,
				entries = info.rawOpmPerSecond.toEntries(),
			),
			lineDataSet(
				labelResId = R.string.generic_opm,
				entries = info.opmPerSecond.toEntries(),
			),
			lineDataSet(
				labelResId = R.string.generic_errors,
				lineAndCirclesColorResId = R.color.red_500,
				entries = info.errorsAtSecond.valuesToEntriesWithYValue(info.errorsYValue.toFloat()),
				isDashedLineEnable = false,
			) {
				circleRadius = 2F
			}
		)

		binding.lytTestChart.lineChart.applyDefault(dataSets = lineDataSets)
		{
			xAxis.granularity = 0.5F
		}

		binding.lytTestChart.lineChart.avoidConflictsWithScroll(binding.root)
	}

	private fun initStats(info: TestStatsInfo) = using(info)
	{
		val listOfLabeledData = listOf(
			R.string.generic_operations labelOf operationCount,
			R.string.generic_totalTime labelOf timeInSeconds,
			R.string.generic_opm labelOf opm,
			R.string.generic_raw labelOf rawOpm,
			R.string.frgHome_highestOpm labelOf maxOpm,
			R.string.frgHome_highestRawOpm labelOf maxRawOpm,
			R.string.generic_errors labelOf errorCount,
		)

		binding.lytTextInfoList.rvLabeledData.adapter = MainLabeledDataAdapter(listOfLabeledData)
	}

	private fun initFailedOperationsAdapter(info: TestInfo)
	{
		binding.rvFailedOperations.adapter = FailedOperationAdapter(info.listOfFailedOperationInfo)
	}
}
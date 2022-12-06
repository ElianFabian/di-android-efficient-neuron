package com.elian.computeit.feature_tests.presentation.test_end

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.elian.computeit.R
import com.elian.computeit.core.presentation.adapter.LabeledDataAdapter
import com.elian.computeit.core.presentation.model.LabeledData
import com.elian.computeit.core.presentation.util.extensions.avoidConflictsWithScroll
import com.elian.computeit.core.presentation.util.extensions.navigate
import com.elian.computeit.core.presentation.util.mp_android_chart.applyDefault
import com.elian.computeit.core.presentation.util.mp_android_chart.lineDataSet
import com.elian.computeit.core.presentation.util.mp_android_chart.toEntries
import com.elian.computeit.core.presentation.util.mp_android_chart.valuesToEntriesWithYValue
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.constants.EXTRA_TEST_INFO
import com.elian.computeit.core.util.extensions.apply2
import com.elian.computeit.databinding.FragmentTestDetailsBinding
import com.elian.computeit.feature_tests.domain.model.TestInfo
import com.elian.computeit.feature_tests.presentation.test_end.adapter.FailedOperationAdapter
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


	private fun initUi() = binding.apply2()
	{
		val info = arguments?.getParcelable<TestInfo>(EXTRA_TEST_INFO)!!

		initLineChart(info)
		initTextInfo(info)

		if (info.listOfFailedOperationInfo.isEmpty())
		{
			tvFailedOperations.isGone = true
			rvFailedOperations.isGone = true
		}
		else initFailedOperationsAdapter(info)

		btnContinue.setOnClickListener { navigate(R.id.action_testDetailsFragment_to_homeFragment) }
	}

	private fun initLineChart(info: TestInfo)
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
			)
			{
				circleRadius = 2F
			}
		)

		binding.lytTestChart.lineChart.applyDefault(dataSets = lineDataSets)
		{
			xAxis.granularity = 0.5F
		}

		binding.lytTestChart.lineChart.avoidConflictsWithScroll(binding.root)
	}

	private fun initTextInfo(info: TestInfo) = info.apply2()
	{
		val listOfUiLabeledData = listOf(
			LabeledData(
				label = getString(R.string.generic_operations),
				value = "$operationCount",
			),
			LabeledData(
				label = getString(R.string.generic_totalTime),
				value = timeInSeconds,
			),
			LabeledData(
				label = getString(R.string.generic_opm),
				value = "$opm",
			),
			LabeledData(
				label = getString(R.string.generic_raw),
				value = "$rawOpm",
			),
			LabeledData(
				label = getString(R.string.frgHome_highestOpm),
				value = "$maxOpm",
			),
			LabeledData(
				label = getString(R.string.frgHome_highestRawOpm),
				value = "$maxRawOpm",
			),
			LabeledData(
				label = getString(R.string.generic_errors),
				value = "$errorCount",
			),
		)

		binding.lytTextInfoList.rvLabeledData.adapter = LabeledDataAdapter(listOfUiLabeledData)
	}

	private fun initFailedOperationsAdapter(info: TestInfo)
	{
		binding.rvFailedOperations.adapter = FailedOperationAdapter(info.listOfFailedOperationInfo)
	}
}
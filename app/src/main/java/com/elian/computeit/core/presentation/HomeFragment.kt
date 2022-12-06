package com.elian.computeit.core.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elian.computeit.R
import com.elian.computeit.core.presentation.adapter.LabeledDataAdapter
import com.elian.computeit.core.presentation.model.LabeledData
import com.elian.computeit.core.presentation.util.extensions.*
import com.elian.computeit.core.presentation.util.mp_android_chart.applyDefault
import com.elian.computeit.core.presentation.util.mp_android_chart.lineDataSet
import com.elian.computeit.core.presentation.util.mp_android_chart.toEntries
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.constants.DEFAULT_DECIMAL_FORMAT
import com.elian.computeit.core.util.extensions.apply2
import com.elian.computeit.core.util.extensions.format
import com.elian.computeit.databinding.FragmentHomeBinding
import com.elian.computeit.feature_tests.domain.model.TestListInfo
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

		binding.lytTestListChart.lineChart.isGone = true
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
					lineAndCirclesColorResId = R.color.chart_secondary,
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

			binding.lytTestListChart.lineChart.applyDefault(
				animate = !_isUiFinished,
				dataSets = lineDataSets,
			)
		}
		else binding.lytTestListChart.lineChart.apply()
		{
			setNoDataText(getString(R.string.no_data_available))
			setNoDataTextColor(getThemeColor(R.attr.colorSecondary))
		}

		binding.lytTestListChart.lineChart.isVisible = true
		_isUiFinished = true

		binding.lytTestListChart.lineChart.avoidConflictsWithScroll(binding.root)
	}

	private fun initTextInfo(info: TestListInfo) = info.apply2()
	{
		val listOfUiLabeledData = listOf(
			LabeledData(
				label = getString(R.string.frgHome_testsCompleted),
				value = "$testsCompleted",
			),
			LabeledData(
				label = getString(R.string.generic_totalTime),
				value = totalTime,
			),
			LabeledData(
				label = getString(R.string.frgHome_operationsCompleted),
				value = "$operationsCompleted",
			),
			LabeledData(
				label = getString(R.string.frgHome_correctOperationsCompleted),
				value = "$correctOperationsCompleted (${correctOperationsCompletedPercentage.toInt()} %)",
			),
			LabeledData(
				label = getString(R.string.frgHome_averageOpm),
				value = averageOpm.format(DEFAULT_DECIMAL_FORMAT),
			),
			LabeledData(
				label = getString(R.string.frgHome_averageRawOpm),
				value = averageRawOpm.format(DEFAULT_DECIMAL_FORMAT),
			),
			LabeledData(
				label = getString(R.string.frgHome_highestOpm),
				value = "$maxOpm",
			),
			LabeledData(
				label = getString(R.string.frgHome_highestRawOpm),
				value = "$maxRawOpm",
			),
		)

		binding.lytTextInfoList.rvLabeledData.adapter = LabeledDataAdapter(listOfUiLabeledData)
	}
}
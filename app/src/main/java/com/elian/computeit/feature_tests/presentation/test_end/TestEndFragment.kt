package com.elian.computeit.feature_tests.presentation.test_end

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.elian.computeit.R
import com.elian.computeit.core.presentation.adapter.GenericAdapter
import com.elian.computeit.core.presentation.util.extensions.navigate
import com.elian.computeit.core.presentation.util.mp_android_chart.applyDefault
import com.elian.computeit.core.presentation.util.mp_android_chart.lineDataSet
import com.elian.computeit.core.presentation.util.mp_android_chart.toEntries
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.constants.EXTRA_TEST_INFO
import com.elian.computeit.core.util.extensions.apply2
import com.elian.computeit.databinding.FragmentTestEndBinding
import com.elian.computeit.databinding.ItemFailedOperationBinding
import com.elian.computeit.feature_tests.domain.model.TestInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestEndFragment : Fragment(R.layout.fragment_test_end)
{
	private val binding by viewBinding(FragmentTestEndBinding::bind)


	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		initUi()
	}


	private fun initUi() = binding.apply2()
	{
		val info = arguments?.getParcelable<TestInfo>(EXTRA_TEST_INFO)!!

		binding.apply()
		{
			info.apply()
			{
				tvOpm.text = "$opm"
				tvRawOpm.text = "$rawOpm"
				tvTime.text = "$timeInSeconds s"
				tvOperations.text = "$operationCount"
				tvErrors.text = "$errorCount"

				if (listOfFailedOperationInfo.isEmpty())
				{
					tvFailedOperations.isGone = true
					rvFailedOperations.isGone = true
				}
			}
		}

		initLineChart(info)
		initRecyclerView(info)

		btnContinue.setOnClickListener { navigate(R.id.action_testEndFragment_to_homeFragment) }
	}


	private fun initLineChart(info: TestInfo)
	{
		val lineDataSets = arrayOf(
			lineDataSet(
				labelResId = R.string.generic_raw,
				lineAndCirclesColorResId = R.color.default_chart_25,
				entries = info.rawOpmPerSecond.toEntries(),
			),
			lineDataSet(
				labelResId = R.string.generic_opm,
				entries = info.opmPerSecond.toEntries(),
			),
		)

		binding.lcTestGraph.applyDefault(dataSets = lineDataSets)
	}

	private fun initRecyclerView(info: TestInfo)
	{
		val adapter = GenericAdapter(
			list = info.listOfFailedOperationInfo,
			inflate = ItemFailedOperationBinding::inflate,
		) {
			tvFirstNumber.text = "${it.pairOfNumbers.first}"
			tvOperationSymbol.text = it.operationSymbol
			tvSecondNumber.text = "${it.pairOfNumbers.second}"
			tvInsertedResult.text = "${it.insertedResult}"
			tvExpectedResult.text = "${it.expectedResult}"
		}

		binding.rvFailedOperations.adapter = adapter
	}
}
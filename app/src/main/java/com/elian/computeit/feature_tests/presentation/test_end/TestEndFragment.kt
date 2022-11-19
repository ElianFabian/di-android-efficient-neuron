package com.elian.computeit.feature_tests.presentation.test_end

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elian.computeit.R
import com.elian.computeit.core.domain.models.TestData
import com.elian.computeit.core.presentation.util.extensions.navigate
import com.elian.computeit.core.presentation.util.mp_android_chart.applyDefault
import com.elian.computeit.core.presentation.util.mp_android_chart.lineDataSet
import com.elian.computeit.core.presentation.util.mp_android_chart.toEntries
import com.elian.computeit.core.util.constants.EXTRA_TEST_DATA
import com.elian.computeit.core.util.extensions.*
import com.elian.computeit.databinding.FragmentTestEndBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestEndFragment : Fragment()
{
    private lateinit var binding: FragmentTestEndBinding
    private lateinit var testData: TestData


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentTestEndBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initUi()
    }


    private fun initData()
    {
        testData = arguments?.getParcelable(EXTRA_TEST_DATA)!!
    }

    private fun initUi()
    {
        binding.apply()
        {
            testData.apply()
            {
                tvOpm.text = "$opm"
                tvRawOpm.text = "$rawOpm"
                tvTime.text = "$testTimeInSeconds s"
                tvTests.text = "${operationDataList.size}"
                tvErrors.text = "$errorCount"
            }

            btnContinue.setOnClickListener { navigate(R.id.action_testEndFragment_to_homeFragment) }
        }

        initLineChart()
    }

    private fun initLineChart()
    {
        binding.lcTestGraph.applyDefault(
            lineDataSet(
                labelResId = R.string.generic_raw,
                lineAndCirclesColor = R.color.default_line_chart_25,
                entries = testData.rawOpmPerSecond.toEntries(),
                context = context,
            ),
            lineDataSet(
                labelResId = R.string.generic_opm,
                entries = testData.opmPerSecond.toEntries(),
                context = context,
            ),
        )
    }
}
package com.elian.computeit.feature_tests.presentation.test_end

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.navigate
import com.elian.computeit.core.presentation.util.mp_android_chart.applyDefault
import com.elian.computeit.core.presentation.util.mp_android_chart.lineDataSet
import com.elian.computeit.core.presentation.util.mp_android_chart.toEntries
import com.elian.computeit.core.util.constants.EXTRA_TEST_INFO
import com.elian.computeit.databinding.FragmentTestEndBinding
import com.elian.computeit.feature_tests.domain.model.TestInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestEndFragment : Fragment()
{
    private lateinit var binding: FragmentTestEndBinding
    private lateinit var testInfo: TestInfo


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

        initUi()
    }

    private fun initUi()
    {
        testInfo = arguments?.getParcelable(EXTRA_TEST_INFO)!!

        binding.apply()
        {
            testInfo.apply()
            {
                tvOpm.text = "$opm"
                tvRawOpm.text = "$rawOpm"
                tvTime.text = "$timeInSeconds s"
                tvOperations.text = "$operationCount"
                tvErrors.text = "$errorCount"
            }

            btnContinue.setOnClickListener { navigate(R.id.action_testEndFragment_to_homeFragment) }
        }

        initLineChart(testInfo)
    }

    private fun initLineChart(testInfo: TestInfo)
    {
        val lineDataSets = arrayOf(
            lineDataSet(
                labelResId = R.string.generic_raw,
                lineAndCirclesColorResId = R.color.default_chart_25,
                entries = testInfo.rawOpmPerSecond.toEntries(),
            ),
            lineDataSet(
                labelResId = R.string.generic_opm,
                entries = testInfo.opmPerSecond.toEntries(),
            ),
        )

        binding.lcTestGraph.applyDefault(dataSets = lineDataSets)
    }
}
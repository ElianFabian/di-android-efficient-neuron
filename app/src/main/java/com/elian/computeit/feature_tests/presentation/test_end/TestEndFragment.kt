package com.elian.computeit.feature_tests.presentation.test_end

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elian.computeit.R
import com.elian.computeit.core.domain.models.TestData
import com.elian.computeit.core.presentation.util.extensions.getColorCompat
import com.elian.computeit.core.presentation.util.extensions.navigate
import com.elian.computeit.core.presentation.util.mp_android_chart.*
import com.elian.computeit.core.util.constants.EXTRA_TEST_DATA
import com.elian.computeit.core.util.extensions.*
import com.elian.computeit.databinding.FragmentTestEndBinding
import com.github.mikephil.charting.data.LineData
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
        binding.apply2()
        {
            testData.apply2()
            {
                tvTpm.text = "$tpm"
                tvRawTpm.text = "$rawTpm"
                tvTime.text = "$testTimeInSeconds s"
                tvTests.text = "${testDataList.size}"
                tvErrors.text = "$errorCount"
            }

            btnContinue.setOnClickListener { navigate(R.id.action_testEndFragment_to_homeFragment) }
        }

        initTpmPerSecondChart()
    }

    private fun initTpmPerSecondChart()
    {
        val tpmSet = lineDataSet(
            context = context,
            entries = testData.tpmPerSecond.toEntries(),
            labelResId = R.string.generic_tpm,
        )
        val rawTpmSet = lineDataSet(
            context = context,
            entries = testData.rawTpmPerSecond.toEntries(),
            labelResId = R.string.generic_raw,
        ) {
            lineAndCirclesColor = context.getColorCompat(R.color.teal_700)
        }

        binding.lcTestGraph.applyDefaultStyle()
        {
            data = LineData(
                rawTpmSet,
                tpmSet,
            )

            animateX(500)

            isInteractionEnable = false
        }
    }
}
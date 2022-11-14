package com.elian.computeit.feature_tests.presentation.test_end

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.getColor2
import com.elian.computeit.core.presentation.util.extensions.navigate
import com.elian.computeit.core.util.constants.EXTRA_TEST_SESSION_DATA
import com.elian.computeit.core.util.extensions.*
import com.elian.computeit.core.presentation.util.mp_android_chart.*
import com.elian.computeit.databinding.FragmentTestEndBinding
import com.elian.computeit.feature_tests.data.models.TestSessionData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestEndFragment : Fragment()
{
    private lateinit var binding: FragmentTestEndBinding
    private lateinit var testSessionData: TestSessionData


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
        testSessionData = arguments?.getParcelable(EXTRA_TEST_SESSION_DATA)!!
    }

    private fun initUi()
    {
        binding.apply()
        {
            testSessionData.apply()
            {
                tvTmp.text = "$speedInTpm"
                tvRawTmp.text = "$rawSpeedInTpm"
                tvTime.text = "$testTimeInSeconds s"
                tvTests.text = "${testDataList.size}"
                tvErrors.text = "$errorCount"
            }

            btnContinue.setOnClickListener { navigate(R.id.action_testEndFragment_to_homeFragment) }
        }

        initChart()
    }

    private fun initChart()
    {
        val tpmSet = LineDataSet(
            testSessionData.speedOverTimeInTpm.toEntries(),
            getString(R.string.generic_tmp)
        ).applyDefaultStyle().apply()
        {
            lineAndCirclesColor = getColor2(R.color.teal_200)
        }

        val rawTpmSet = LineDataSet(
            testSessionData.rawSpeedOverTimeInTpm.toEntries(),
            getString(R.string.generic_raw)
        ).applyDefaultStyle().apply()
        {
            lineAndCirclesColor = getColor2(R.color.teal_700)
        }

        binding.lcTestGraph.applyDefaultStyle().apply()
        {
            setAllData(
                rawTpmSet,
                tpmSet
            )

            animateX(500)

            disableInteraction()
        }
    }
}
package com.elian.computeit.feature_tests.presentation.test_end

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elian.computeit.R
import com.elian.computeit.core.domain.models.TestSessionData
import com.elian.computeit.core.presentation.util.extensions.getColorCompat
import com.elian.computeit.core.presentation.util.extensions.navigate
import com.elian.computeit.core.presentation.util.mp_android_chart.*
import com.elian.computeit.core.util.constants.EXTRA_TEST_SESSION_DATA
import com.elian.computeit.core.util.extensions.errorCount
import com.elian.computeit.core.util.extensions.rawTpm
import com.elian.computeit.core.util.extensions.tpm
import com.elian.computeit.core.util.extensions.tpmPerSecond
import com.elian.computeit.databinding.FragmentTestEndBinding
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
                tvTmp.text = "$tpm"
                tvRawTmp.text = "$rawTpm"
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
        val tpmSet = lineDataSet(
            entries = testSessionData.tpmPerSecond.toEntries(),
            label = getString(R.string.generic_tmp),
            lineAndCirclesColor = getColorCompat(R.color.teal_200)
        )
        val rawTpmSet = lineDataSet(
            entries = testSessionData.tpmPerSecond.toEntries(),
            label = getString(R.string.generic_raw),
            lineAndCirclesColor = getColorCompat(R.color.teal_700)
        )

        binding.lcTestGraph.applyDefaultStyle().apply()
        {
            setAllData(
                rawTpmSet,
                tpmSet
            )

            animateX(500)

            isInteractionEnable = false
        }
    }
}
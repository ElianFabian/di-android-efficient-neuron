package com.elian.computeit.core.presentation.adapter

import android.content.Context
import com.elian.computeit.R
import com.elian.computeit.core.domain.models.TestInfo
import com.elian.computeit.core.presentation.model.labelOf
import com.elian.computeit.core.presentation.util.mp_android_chart.SimpleMarkerView
import com.elian.computeit.databinding.ItemLabeledDataOfTestInfoMarkerBinding
import com.elian.computeit.databinding.MarkerTestInfoBinding
import com.github.mikephil.charting.utils.Utils

@Suppress("FunctionName")
fun TestInfoMarker(
	context: Context?,
	items: List<TestInfo>,
) = SimpleMarkerView(
	context = context,
	inflate = MarkerTestInfoBinding::inflate,
) { entry, _ ->

	val position = entry.x.toInt() - 1

	val uiLabeledData = items[position].statsInfo.run {
		listOf(
			R.string.generic_date labelOf date,
			R.string.generic_operations labelOf operationCount,
			R.string.arrayTest_modes_time labelOf "$timeInSeconds s",
			R.string.generic_opm labelOf opm,
			R.string.generic_raw labelOf rawOpm,
			R.string.generic_errors labelOf errorCount,
		)
	}

	rvLabeledData.adapter = BaseLabeledDataAdapter(
		items = uiLabeledData,
		inflate = ItemLabeledDataOfTestInfoMarkerBinding::inflate,
		getLabel = { tvLabel },
		getValue = { tvValue },
		afterBind = { tvLabel.text = "${tvLabel.text}: " },
	)
}.apply {
	offset.y -= Utils.convertDpToPixel(binding.root.measuredHeight.toFloat() * 2)
}
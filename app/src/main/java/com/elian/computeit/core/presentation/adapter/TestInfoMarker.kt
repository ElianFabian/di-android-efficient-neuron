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
			R.string.Date labelOf date,
			R.string.Operations labelOf operationCount,
			R.string.Time labelOf "$timeInSeconds s",
			R.string.OPM__OperationsPerMinute labelOf opm.toInt(),
			R.string.Raw labelOf rawOpm.toInt(),
			R.string.Errors labelOf errorCount,
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
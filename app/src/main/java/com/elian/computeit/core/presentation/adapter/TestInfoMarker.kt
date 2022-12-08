package com.elian.computeit.core.presentation.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import com.elian.computeit.R
import com.elian.computeit.core.presentation.model.LabeledData
import com.elian.computeit.core.presentation.util.mp_android_chart.GenericMarkerView
import com.elian.computeit.databinding.MarkerTestInfoBinding
import com.elian.computeit.feature_tests.domain.model.TestInfo
import com.github.mikephil.charting.utils.Utils


@Suppress("FunctionName", "UNCHECKED_CAST")
fun TestInfoMarker(context: Context?) = GenericMarkerView(
	context = context,
	inflate = MarkerTestInfoBinding::inflate,
) { entry, _ ->

	val uiLabeledData = (entry.data as? TestInfo)?.run()
	{
		listOf(
			LabeledData(
				label = context!!.getString(R.string.generic_date),
				value = date,
			),
			LabeledData(
				label = context.getString(R.string.generic_operations),
				value = operationCount,
			),
			LabeledData(
				label = context.getString(R.string.arrayTest_modes_time),
				value = timeInSeconds,
			),
			LabeledData(
				label = context.getString(R.string.generic_opm),
				value = opm,
			),
			LabeledData(
				label = context.getString(R.string.generic_raw),
				value = rawOpm,
			),
			LabeledData(
				label = context.getString(R.string.generic_errors),
				value = errorCount,
			),
		)
	} ?: emptyList()

	rvLabeledData.adapter = LabeledDataAdapter(
		list = uiLabeledData,
		setStyle = {
			root.orientation = LinearLayoutCompat.HORIZONTAL
			root.layoutParams = ViewGroup.MarginLayoutParams(root.layoutParams).apply()
			{
				topMargin = 5
				bottomMargin = 5
			}

			tvLabel.setTypeface(null, Typeface.BOLD)
		},
		afterBind = {
			tvLabel.text = "${tvLabel.text}: "
		},
	)
}.apply()
{
	offset.y -= Utils.convertDpToPixel(binding.root.measuredHeight.toFloat() * 2)
}
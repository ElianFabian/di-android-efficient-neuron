package com.elian.computeit.core.presentation.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import com.elian.computeit.R
import com.elian.computeit.core.presentation.model.withLabel
import com.elian.computeit.core.presentation.util.mp_android_chart.GenericMarkerView
import com.elian.computeit.databinding.MarkerTestInfoBinding
import com.elian.computeit.feature_tests.domain.model.TestInfo
import com.github.mikephil.charting.utils.Utils


@Suppress("FunctionName", "UNCHECKED_CAST")
fun TestInfoMarker(context: Context?) = GenericMarkerView(
	context = context,
	inflate = MarkerTestInfoBinding::inflate,
) { entry, _ ->

	val uiLabeledData = (entry.data as? TestInfo)?.statsInfo?.run()
	{
		listOf(
			date withLabel R.string.generic_date,
			operationCount withLabel R.string.generic_operations,
			timeInSeconds withLabel R.string.arrayTest_modes_time,
			opm withLabel R.string.generic_opm,
			rawOpm withLabel R.string.generic_raw,
			errorCount withLabel R.string.generic_errors,
		)
	} ?: emptyList()

	rvLabeledData.adapter = LabeledDataAdapter(
		items = uiLabeledData,
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
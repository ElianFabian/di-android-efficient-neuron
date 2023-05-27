package com.elian.computeit.core.presentation.util.mp_android_chart

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.getColorCompat
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

fun Fragment.lineDataSet(
	entries: List<Entry>,
	label: String? = null,
	@StringRes labelResId: Int = 0,
	@ColorRes lineAndCirclesColorResId: Int = R.color.default_chart,
	isDashedLineEnable: Boolean = true,
	block: (LineDataSet.() -> Unit)? = null,
): LineDataSet {
	return LineDataSet(entries, label ?: getString(labelResId)).apply {
		if (!isDashedLineEnable) enableDashedLine(0F, 1F, 0F)

		setDrawValues(false)
		setDrawHorizontalHighlightIndicator(false)
		setDrawVerticalHighlightIndicator(false)

		this.lineAndCirclesColor = getColorCompat(lineAndCirclesColorResId)

		mode = LineDataSet.Mode.CUBIC_BEZIER
		cubicIntensity = 0.2F
		lineWidth = 2.2F
		circleRadius = 3F

		block?.invoke(this)
	}
}

fun Fragment.barDataSet(
	entries: List<BarEntry>,
	label: String? = null,
	@StringRes labelResId: Int = 0,
	@ColorRes colorResId: Int = R.color.teal_200,
	block: (BarDataSet.() -> Unit)? = null,
): BarDataSet {
	return BarDataSet(entries, label ?: getString(labelResId)).apply {
		setDrawValues(false)

		color = getColorCompat(colorResId)

		block?.invoke(this)
	}
}

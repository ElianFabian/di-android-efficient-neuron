package com.elian.computeit.core.presentation.util.mp_android_chart

import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.getThemeColor
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.IMarker

var Chart<*>.marker2: IMarker
	get() = marker as GenericMarkerView<*>
	set(value)
	{
		marker = value
		(value as GenericMarkerView<*>).chartView = this
	}

fun Chart<*>.showNoDataText()
{
	setNoDataText(context!!.getString(R.string.no_data_available))
	setNoDataTextColor(context!!.getThemeColor(R.attr.colorSecondary))
	getPaint(Chart.PAINT_INFO).textSize = resources.getDimension(R.dimen.textSize_medium2)
}
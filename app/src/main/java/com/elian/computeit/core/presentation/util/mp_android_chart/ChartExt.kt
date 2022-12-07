package com.elian.computeit.core.presentation.util.mp_android_chart

import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.IMarker

var Chart<*>.marker2: IMarker
	get() = marker as GenericMarkerView<*>
	set(value)
	{
		marker = value
		(value as GenericMarkerView<*>).chartView = this
	}
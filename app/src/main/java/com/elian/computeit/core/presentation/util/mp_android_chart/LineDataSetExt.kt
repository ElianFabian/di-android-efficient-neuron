package com.elian.computeit.core.presentation.util.mp_android_chart

import androidx.annotation.ColorInt
import com.github.mikephil.charting.data.LineDataSet

var LineDataSet.lineAndCirclesColor: Int?
	get() {
		if (color == circleHoleColor && color == getCircleColor(0)) return color

		return null
	}
	set(@ColorInt value) {
		if (value == null) return

		setCircleColor(value)
		circleHoleColor = value
		color = value
	}
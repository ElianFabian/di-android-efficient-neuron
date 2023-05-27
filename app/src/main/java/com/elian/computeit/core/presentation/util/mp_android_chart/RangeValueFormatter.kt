package com.elian.computeit.core.presentation.util.mp_android_chart

import com.github.mikephil.charting.formatter.ValueFormatter

class RangeValueFormatter(
	var rangeLength: Int = 10,
	var minOpm: Float = 0F,
	var maxOpm: Float = Float.MAX_VALUE,
) : ValueFormatter() {
	override fun getFormattedValue(value: Float): String {
		val start = (rangeLength * value + minOpm).toInt()
		val end = (rangeLength * (value + 1) - 1 + minOpm).toInt()

		return when {
			start.toFloat() == maxOpm || rangeLength == 1 -> "$start"
			start < minOpm && end > maxOpm                -> "${minOpm.toInt()}-${maxOpm.toInt()}"
			start < minOpm                                -> "${minOpm.toInt()}-$end"
			end > maxOpm                                  -> "$start-${maxOpm.toInt()}"
			else                                          -> "$start−$end"
		}
	}
}
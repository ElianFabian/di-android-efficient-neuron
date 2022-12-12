package com.elian.computeit.core.presentation.util.mp_android_chart

import com.github.mikephil.charting.formatter.ValueFormatter

class RangeValueFormatter(
	var rangeLength: Int = 10,
	var maxLength: Int = Int.MAX_VALUE,
) : ValueFormatter()
{
	override fun getFormattedValue(value: Float): String
	{
		val valueToInt = value.toInt()

		val start = rangeLength * valueToInt
		val end = rangeLength * (valueToInt + 1) - 1

		return when
		{
			start == maxLength || rangeLength == 1 -> "$start"
			end > maxLength                        -> "$start-$maxLength"
			else                                   -> "$start−$end"
		}
	}
}
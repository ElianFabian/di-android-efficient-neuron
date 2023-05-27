package com.elian.computeit.core.presentation.util.mp_android_chart

import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry

fun Map<out Number, Number>.toEntries() = this.map { Entry(it.key.toFloat(), it.value.toFloat()) }

fun List<Number>.toEntries(startXValue: Int = 0, listOfData: List<Any>? = null) = this.mapIndexed { index, number ->

	Entry(index.toFloat() + startXValue, number.toFloat()).apply {
		this.data = listOfData?.getOrNull(index)
	}
}

fun List<Number>.valuesToEntriesWithYValue(yValue: Float) = this.map { Entry(it.toFloat(), yValue) }


fun List<Number>.toBarEntries(startXValue: Int = 0) = this.mapIndexed { index, number ->

	BarEntry(index.toFloat() + startXValue, number.toFloat())
}
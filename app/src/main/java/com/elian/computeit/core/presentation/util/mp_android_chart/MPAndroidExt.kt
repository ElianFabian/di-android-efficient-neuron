package com.elian.computeit.core.presentation.util.mp_android_chart

import com.github.mikephil.charting.data.Entry

fun Map<out Number, Number>.toEntries() = this.map { Entry(it.key.toFloat(), it.value.toFloat()) }

fun List<Number>.toEntries(firstValue: Int = 1, listOfData: List<Any>? = null) = this.mapIndexed { index, number ->

	Entry(index.toFloat() + firstValue, number.toFloat()).apply()
	{
		this.data = listOfData?.getOrNull(index)
	}
}

fun List<Number>.valuesToEntriesWithYValue(yValue: Float) = this.map { Entry(it.toFloat(), yValue) }
package com.elian.computeit.core.presentation.util.mp_android_chart

import android.graphics.Color
import com.elian.computeit.core.util.extensions.orZero
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlin.math.ln

var BarChart.isInteractionEnable: Boolean
	get() = isDoubleTapToZoomEnabled && isScaleXEnabled
	set(value) {
		isDoubleTapToZoomEnabled = value
		setScaleEnabled(value)
		setTouchEnabled(value)
	}

fun BarChart.applyDefaultStyle(block: (BarChart.() -> Unit)? = null): BarChart {
	setDrawGridBackground(false)
	description.isEnabled = false
	axisRight.isEnabled = false

	xAxis.apply {
		setDrawGridLines(false)
		granularity = 1F
		this.textColor = Color.WHITE
	}
	axisLeft.apply {
		setDrawGridLines(false)
		granularity = 1F
		this.textColor = Color.WHITE
	}
	legend.apply {
		this.textColor = Color.WHITE
	}

	axisLeft.axisMinimum = 0F

	block?.invoke(this)

	return this
}

fun BarChart.applyDefaultConfiguration(block: (BarChart.() -> Unit)? = null): BarChart {
	isDoubleTapToZoomEnabled = false

	block?.invoke(this)

	return this
}

fun BarChart.applyDefaultAnimation(block: (BarChart.() -> Unit)? = null): BarChart {
	val largestDataSetCount = this.data.dataSets.maxOfOrNull { it.entryCount }.orZero()

	val animationTime = ln(largestDataSetCount.toFloat() + 1).toInt() * 150

	animateX(animationTime)

	block?.invoke(this)

	return this
}

fun BarChart.applyDefault(
	vararg dataSets: IBarDataSet,
	block: (BarChart.() -> Unit)? = null,
): BarChart {
	if (dataSets.isNotEmpty()) {
		data = BarData(*dataSets)
	}

	applyDefaultStyle()
	applyDefaultConfiguration()

	block?.invoke(this)

	return this
}

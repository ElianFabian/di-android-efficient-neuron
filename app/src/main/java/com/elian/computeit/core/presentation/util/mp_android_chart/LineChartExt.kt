package com.elian.computeit.core.presentation.util.mp_android_chart

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

var LineChart.isInteractionEnable: Boolean
    get() = isDoubleTapToZoomEnabled && isScaleXEnabled
    set(value)
    {
        isDoubleTapToZoomEnabled = value
        setScaleEnabled(value)
        setTouchEnabled(value)
    }

fun LineChart.applyDefaultStyle(block: (LineChart.() -> Unit)? = null): LineChart
{
    setDrawGridBackground(false)
    description.isEnabled = false
    axisRight.isEnabled = false

    xAxis.apply()
    {
        setDrawGridLines(false)
        granularity = 1F
        this.textColor = Color.WHITE
    }
    axisLeft.apply()
    {
        setDrawGridLines(false)
        granularity = 1F
        this.textColor = Color.WHITE
    }
    legend.apply()
    {
        this.textColor = Color.WHITE
    }

    block?.invoke(this)

    return this
}

fun LineChart.applyDefaultConfiguration(block: (LineChart.() -> Unit)? = null): LineChart
{
    isDoubleTapToZoomEnabled = false

    block?.invoke(this)

    return this
}

fun LineChart.applyDefaultAnimation(block: (LineChart.() -> Unit)? = null): LineChart
{
    val largestDataSetCount = this.data.dataSets.maxOfOrNull { it.entryCount } ?: 0

    val animationTime = largestDataSetCount * 25

    animateX(animationTime)

    block?.invoke(this)

    return this
}

fun LineChart.applyDefault(
    vararg dataSets: ILineDataSet,
    block: (LineChart.() -> Unit)? = null,
): LineChart
{
    data = LineData(*dataSets)

    applyDefaultStyle()
    applyDefaultConfiguration()
    applyDefaultAnimation()

    block?.invoke(this)

    return this
}
    
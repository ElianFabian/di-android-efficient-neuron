package com.elian.computeit.core.presentation.util.mp_android_chart

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart

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

    isDoubleTapToZoomEnabled = false

    return this
}
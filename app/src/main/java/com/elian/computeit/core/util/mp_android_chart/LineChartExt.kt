package com.elian.computeit.core.util.mp_android_chart

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

fun LineChart.disableInteraction()
{
    isDoubleTapToZoomEnabled = false
    setScaleEnabled(false)
    setTouchEnabled(false)
}

fun LineChart.applyDefaultStyle(): LineChart
{
    setDrawGridBackground(false)
    description.isEnabled = false
    axisRight.isEnabled = false

    xAxis.apply()
    {
        setDrawGridLines(false)
        textColor = Color.WHITE
    }
    axisLeft.apply()
    {
        setDrawGridLines(false)
        textColor = Color.WHITE
    }
    legend.apply()
    {
        textColor = Color.WHITE
    }

    return this
}

fun LineChart.setAllData(vararg dataSets: ILineDataSet?)
{
    data = LineData()
    dataSets.forEach { data.addDataSet(it) }
}
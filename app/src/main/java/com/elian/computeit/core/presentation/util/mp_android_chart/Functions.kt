package com.elian.computeit.core.presentation.util.mp_android_chart

import android.graphics.Color
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

fun lineDataSet(
    entries: List<Entry>,
    label: String,
    lineDataSetBlock: (LineDataSet.() -> Unit)? = null,
): LineDataSet
{
    return LineDataSet(entries, label).applyDefaultStyle().apply()
    {
        this.lineAndCirclesColor = Color.parseColor("#03dac5")
        lineDataSetBlock?.invoke(this)
    }
}

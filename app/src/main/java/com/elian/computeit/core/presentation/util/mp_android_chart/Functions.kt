package com.elian.computeit.core.presentation.util.mp_android_chart

import androidx.annotation.ColorInt
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

fun lineDataSet(
    entries: List<Entry>,
    label: String,
    @ColorInt
    lineAndCirclesColor: Int,
): LineDataSet
{
    return LineDataSet(entries, label).applyDefaultStyle().apply()
    {
        this.lineAndCirclesColor = lineAndCirclesColor
    }
}

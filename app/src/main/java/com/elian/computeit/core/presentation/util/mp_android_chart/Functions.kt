package com.elian.computeit.core.presentation.util.mp_android_chart

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.getColorCompat
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

fun lineDataSet(
    context: Context?,
    entries: List<Entry>,
    label: String? = null,
    @StringRes labelResId: Int = 0,
    @ColorRes lineAndCirclesColor: Int = R.color.default_line_chart,
    block: (LineDataSet.() -> Unit)? = null,
): LineDataSet
{
    return LineDataSet(entries, label ?: context!!.getString(labelResId)).apply()
    {
        setDrawValues(false)
        setDrawHorizontalHighlightIndicator(false)
        setDrawVerticalHighlightIndicator(false)

        this.lineAndCirclesColor = context.getColorCompat(lineAndCirclesColor)
        mode = LineDataSet.Mode.CUBIC_BEZIER
        cubicIntensity = 0.2F
        lineWidth = 2.2F
        circleRadius = 3F

        block?.invoke(this)
    }
}

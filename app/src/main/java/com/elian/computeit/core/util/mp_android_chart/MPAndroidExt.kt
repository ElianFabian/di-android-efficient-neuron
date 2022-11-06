package com.elian.computeit.core.util.mp_android_chart

import com.github.mikephil.charting.data.Entry

fun Map<out Number, Number>.toEntries() = this.map { Entry(it.key.toFloat(), it.value.toFloat()) }
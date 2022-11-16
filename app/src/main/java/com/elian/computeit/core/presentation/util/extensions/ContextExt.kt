package com.elian.computeit.core.presentation.util.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context?.getColorCompat(@ColorRes id: Int) = ContextCompat.getColor(this!!, id)
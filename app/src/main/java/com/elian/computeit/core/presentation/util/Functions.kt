package com.elian.computeit.core.presentation.util

import android.view.View
import androidx.core.view.isGone

fun showViews(vararg views: View) = views.forEach { it.isGone = false }
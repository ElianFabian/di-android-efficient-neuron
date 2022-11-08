package com.elian.computeit.core.util.extensions

import android.view.View
import android.view.animation.AlphaAnimation

fun View.startFadeAnimation(
    fromAlpha: Float,
    toAlpha: Float,
    duration: Long,
) {
    val fadeAnimation = AlphaAnimation(fromAlpha, toAlpha).also()
    {
        it.duration = duration
        it.fillAfter = true
    }
    
    startAnimation(fadeAnimation)
}
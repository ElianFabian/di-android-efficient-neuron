package com.elian.computeit.core.util.extensions

import android.view.View
import android.view.animation.AlphaAnimation

fun View.startAlphaAnimation(
    fromAlpha: Float,
    toAlpha: Float,
    duration: Long,
)
{
    val fadeAnimation = AlphaAnimation(fromAlpha, toAlpha).also()
    {
        it.duration = duration
        it.fillAfter = true
    }

    startAnimation(fadeAnimation)
}

fun View.setOnClickListenerOnlyOnce(listener: View.OnClickListener) = setOnClickListener()
{
    listener.onClick(it)
    setOnClickListener(null)
}
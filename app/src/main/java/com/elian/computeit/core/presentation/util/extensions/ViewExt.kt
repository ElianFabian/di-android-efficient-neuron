package com.elian.computeit.core.presentation.util.extensions

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.core.widget.NestedScrollView

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

@SuppressLint("ClickableViewAccessibility")
fun View.avoidConflictsWithScroll(scrollView: NestedScrollView)
{
	setOnTouchListener { _, event ->
		when (event.action)
		{
			MotionEvent.ACTION_DOWN                          -> scrollView.requestDisallowInterceptTouchEvent(true)

			MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> scrollView.requestDisallowInterceptTouchEvent(false)
		}
		false
	}
}
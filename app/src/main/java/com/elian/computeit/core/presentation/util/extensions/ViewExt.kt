package com.elian.computeit.core.presentation.util.extensions

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.core.widget.NestedScrollView

fun View.onClick(action: (view: View) -> Unit) {
	setOnClickListener(action)
}

inline fun View.onScrollChanged(
	crossinline onScrollChanged: (scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) -> Unit,
) {
	setOnScrollChangeListener { _, scrollX, scrollY, oldScrollX, oldScrollY ->
		onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY)
	}
}

fun View.startAlphaAnimation(
	fromAlpha: Float,
	toAlpha: Float,
	durationMillis: Long,
	onAnimationEnd: (() -> Unit)? = null,
) {
	val fadeAnimation = AlphaAnimation(fromAlpha, toAlpha).also {
		it.duration = durationMillis
		it.fillAfter = true

		it.setAnimationListener(object : Animation.AnimationListener {
			override fun onAnimationStart(p0: Animation?) {}

			override fun onAnimationEnd(p0: Animation?) {
				onAnimationEnd?.invoke()
			}

			override fun onAnimationRepeat(p0: Animation?) {}
		})
	}

	startAnimation(fadeAnimation)
}

fun View.setOnClickListenerOnlyOnce(listener: View.OnClickListener) = setOnClickListener {
	listener.onClick(it)
	setOnClickListener(null)
}

@SuppressLint("ClickableViewAccessibility")
fun View.avoidConflictsWithScroll(scrollView: NestedScrollView) {
	setOnTouchListener { _, event ->
		when (event.action) {
			MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> scrollView.requestDisallowInterceptTouchEvent(true)

			MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> scrollView.requestDisallowInterceptTouchEvent(false)
		}
		false
	}
}
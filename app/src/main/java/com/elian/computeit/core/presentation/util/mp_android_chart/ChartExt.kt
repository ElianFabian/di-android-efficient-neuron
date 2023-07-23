package com.elian.computeit.core.presentation.util.mp_android_chart

import android.view.MotionEvent
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.getThemeColor
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

var Chart<*>.marker2: IMarker
	get() = marker as SimpleMarkerView<*>
	set(value) {
		marker = value
		(value as SimpleMarkerView<*>).chartView = this
	}

fun Chart<*>.showNoDataText() {
	setNoDataText(context!!.getString(R.string.NoDataAvailable))
	setNoDataTextColor(context!!.getThemeColor(R.attr.colorSecondary))
	getPaint(Chart.PAINT_INFO).textSize = resources.getDimension(R.dimen.textSize_medium2)
}

inline fun Chart<*>.setOnChartGestureListener(
	crossinline onChartGestureStart: (me: MotionEvent, lastPerformedGesture: ChartTouchListener.ChartGesture?) -> Unit = { _, _ -> },
	crossinline onChartGestureEnd: (me: MotionEvent, lastPerformedGesture: ChartTouchListener.ChartGesture?) -> Unit = { _, _ -> },
	crossinline onChartLongPressed: (me: MotionEvent) -> Unit = {},
	crossinline onChartDoubleTapped: (me: MotionEvent) -> Unit = {},
	crossinline onChartSingleTapped: (me: MotionEvent) -> Unit = {},
	crossinline onChartFling: (me1: MotionEvent, me2: MotionEvent, velocityX: Float, velocityY: Float) -> Unit = { _, _, _, _ -> },
	crossinline onChartScale: (me: MotionEvent, scaleX: Float, scaleY: Float) -> Unit = { _, _, _ -> },
	crossinline onChartTranslate: (me: MotionEvent, dX: Float, dY: Float) -> Unit = { _, _, _ -> }
) {
	val gestureListener = object : OnChartGestureListener {
		override fun onChartGestureStart(me: MotionEvent, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
			onChartGestureStart.invoke(me, lastPerformedGesture)
		}

		override fun onChartGestureEnd(me: MotionEvent, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
			onChartGestureEnd.invoke(me, lastPerformedGesture)
		}

		override fun onChartLongPressed(me: MotionEvent) {
			onChartLongPressed.invoke(me)
		}

		override fun onChartDoubleTapped(me: MotionEvent) {
			onChartDoubleTapped.invoke(me)
		}

		override fun onChartSingleTapped(me: MotionEvent) {
			onChartSingleTapped.invoke(me)
		}

		override fun onChartFling(me1: MotionEvent, me2: MotionEvent, velocityX: Float, velocityY: Float) {
			onChartFling.invoke(me1, me2, velocityX, velocityY)
		}

		override fun onChartScale(me: MotionEvent, scaleX: Float, scaleY: Float) {
			onChartScale.invoke(me, scaleX, scaleY)
		}

		override fun onChartTranslate(me: MotionEvent, dX: Float, dY: Float) {
			onChartTranslate.invoke(me, dX, dY)
		}
	}

	onChartGestureListener = gestureListener
}

inline fun Chart<*>.setOnChartValueSelectedListener(
	crossinline onValueSelected: (entry: Entry, highlight: Highlight) -> Unit = { _, _ -> },
	crossinline onNothingSelected: () -> Unit = {},
) {
	setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
		override fun onValueSelected(e: Entry, h: Highlight) {
			onValueSelected(e, h)
		}

		override fun onNothingSelected() {
			onNothingSelected()
		}
	})
}
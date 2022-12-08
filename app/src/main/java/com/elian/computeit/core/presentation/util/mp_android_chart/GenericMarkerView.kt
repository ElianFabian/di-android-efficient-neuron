package com.elian.computeit.core.presentation.util.mp_android_chart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.viewbinding.ViewBinding
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.lang.ref.WeakReference

/**
 * This is a kotlin version of:
 * @see com.github.mikephil.charting.components.MarkerView class.
 */
@SuppressLint("ViewConstructor")
class GenericMarkerView<VB : ViewBinding>(
	context: Context?,
	inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
	private val onBind: VB.(entry: Entry, highlight: Highlight) -> Unit,
) : RelativeLayout(context), IMarker
{
	private var mOffset = MPPointF()
	private val mOffset2 = MPPointF()
	private var mWeakChart: WeakReference<Chart<*>>? = null
	private lateinit var _binding: VB
	
	val binding get() = _binding

	var chartView: Chart<*>?
		get() = if (mWeakChart == null) null else mWeakChart!!.get()
		set(value)
		{
			mWeakChart = WeakReference(value)
		}

	/**
	 * Constructor. Sets up the GenericMarkerView with a custom layout resource.
	 *
	 * @param context
	 * @param layoutResource the layout resource to use for the GenericMarkerView
	 */
	init
	{
		setupLayoutResource(inflate)
	}


	private fun setupLayoutResource(inflate: (LayoutInflater, ViewGroup, Boolean) -> VB)
	{
		_binding = inflate(LayoutInflater.from(context), this, true)

		val root = _binding.root

		root.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
		root.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))

		// measure(getWidth(), getHeight());
		root.layout(0, 0, root.measuredWidth, root.measuredHeight)
	}

	fun setOffset(offset: MPPointF)
	{
		mOffset = offset
	}

	fun setOffset(offsetX: Float, offsetY: Float)
	{
		mOffset.x = offsetX
		mOffset.y = offsetY
	}

	override fun getOffset(): MPPointF = mOffset

	override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF
	{
		val offset = offset
		mOffset2.x = offset.x
		mOffset2.y = offset.y
		val chart = chartView
		val width = width.toFloat()
		val height = height.toFloat()
		if (posX + mOffset2.x < 0)
		{
			mOffset2.x = -posX
		}
		else if (chart != null && posX + width + mOffset2.x > chart.width)
		{
			mOffset2.x = chart.width - posX - width
		}
		if (posY + mOffset2.y < 0)
		{
			mOffset2.y = -posY
		}
		else if (chart != null && posY + height + mOffset2.y > chart.height)
		{
			mOffset2.y = chart.height - posY - height
		}
		return mOffset2
	}

	override fun refreshContent(entry: Entry, highlight: Highlight)
	{
		onBind(_binding, entry, highlight)

		measure(
			MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
			MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
		)
		layout(0, 0, measuredWidth, measuredHeight)
	}

	override fun draw(canvas: Canvas, posX: Float, posY: Float)
	{
		val offset = getOffsetForDrawingAtPoint(posX, posY)
		val saveId = canvas.save()
		// translate to the correct position and draw
		canvas.translate(posX + offset.x, posY + offset.y)
		draw(canvas)
		canvas.restoreToCount(saveId)
	}
}
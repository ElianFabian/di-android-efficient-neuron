package com.elian.computeit.core.presentation.util.extensions

import android.graphics.BitmapFactory
import android.widget.ImageView

fun ImageView.setImageBytes(bytes: ByteArray)
{
	val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

	setImageBitmap(bitmap)
}
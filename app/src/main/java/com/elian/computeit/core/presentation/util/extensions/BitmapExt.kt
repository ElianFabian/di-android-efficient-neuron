package com.elian.computeit.core.presentation.util.extensions

import android.graphics.Bitmap
import android.media.ThumbnailUtils
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun Bitmap.toBytes(): ByteArray {
	val stream = ByteArrayOutputStream()
	this.compress(Bitmap.CompressFormat.PNG, 100, stream)
	val byteArray = stream.toByteArray()
	this.recycle()

	return byteArray
}

fun Bitmap.cropToSquare(): Bitmap {
	val shortLength = minOf(width, height)

	return ThumbnailUtils.extractThumbnail(this, shortLength, shortLength, ThumbnailUtils.OPTIONS_RECYCLE_INPUT)
}

// From: https://github.com/Vysh01/AndroidImageResizer/blob/master/ImageResizer.java
fun Bitmap.reduceSize(maxSize: Int): Bitmap {
	val ratioSquare = (width * height / maxSize).toDouble()

	if (ratioSquare <= 1) return this

	val ratio = sqrt(ratioSquare)
	val requiredWidth = (width / ratio).roundToInt()
	val requiredHeight = (height / ratio).roundToInt()

	return Bitmap.createScaledBitmap(this, requiredWidth, requiredHeight, true)
}
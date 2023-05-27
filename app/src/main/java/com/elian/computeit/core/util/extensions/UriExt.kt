package com.elian.computeit.core.util.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

fun Uri.toBytes(context: Context?): ByteArray {
	val inputStream = context!!.contentResolver.openInputStream(this)

	val byteBuffer = ByteArrayOutputStream()
	val bufferSize = 1024
	val buffer = ByteArray(bufferSize)

	var len: Int
	while (inputStream!!.read(buffer).also { len = it } != -1) {
		byteBuffer.write(buffer, 0, len)
	}
	return byteBuffer.toByteArray()
}

fun Uri.toBitmap(context: Context?): Bitmap = when {
	Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(context!!.contentResolver, this)
	else                       -> {
		val source = ImageDecoder.createSource(context!!.contentResolver, this)
		ImageDecoder.decodeBitmap(source)
	}
}


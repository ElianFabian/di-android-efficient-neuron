package com.elian.computeit.core.util.extensions

import android.content.Context
import android.net.Uri
import java.io.ByteArrayOutputStream

fun Uri.toBytes(context: Context?): ByteArray
{
	val inputStream = context!!.contentResolver.openInputStream(this)

	val byteBuffer = ByteArrayOutputStream()
	val bufferSize = 1024
	val buffer = ByteArray(bufferSize)

	var len: Int
	while (inputStream!!.read(buffer).also { len = it } != -1)
	{
		byteBuffer.write(buffer, 0, len)
	}
	return byteBuffer.toByteArray()
}
package com.elian.computeit.core.util.constants

import com.elian.computeit.core.util.extensions.format
import java.text.SimpleDateFormat
import java.util.*

private fun dateFormat(pattern: String) = SimpleDateFormat(pattern, Locale.getDefault())


val profileDateFormat = dateFormat("dd/MM/yyyy")


const val DEFAULT_DECIMAL_FORMAT = "%.2f"


fun secondsToDHHMMSS(seconds: Int): String
{
	val days = seconds / (60 * 60 * 24)
	val hours = seconds / (60 * 60)
	val minutes = seconds / (60)

	val hh = (hours % 24).format("%02d")
	val mm = (minutes % 60).format("%02d")
	val ss = (seconds % 60).format("%02d")

	return "$days:$hh:$mm:$ss"
}
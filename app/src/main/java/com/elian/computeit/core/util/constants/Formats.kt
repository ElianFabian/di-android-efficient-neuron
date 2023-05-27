package com.elian.computeit.core.util.constants

import com.elian.computeit.core.util.extensions.formatWith
import java.text.SimpleDateFormat
import java.util.*

private fun dateFormat(pattern: String) = SimpleDateFormat(pattern, Locale.getDefault())


val defaultDateFormat = dateFormat("dd/MM/yyyy")
val defaultFullDateFormat = dateFormat("dd/MM/yyyy HH:mm")


fun secondsToDhhmmss(seconds: Int): String {
	val days = seconds / (60 * 60 * 24)
	val hours = seconds / (60 * 60)
	val minutes = seconds / (60)

	val hh = (hours % 24).formatWith("%02d")
	val mm = (minutes % 60).formatWith("%02d")
	val ss = (seconds % 60).formatWith("%02d")

	return "$days:$hh:$mm:$ss"
}
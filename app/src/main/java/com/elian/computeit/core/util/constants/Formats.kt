package com.elian.computeit.core.util.constants

import java.text.SimpleDateFormat
import java.util.*

private fun dateFormat(pattern: String) = SimpleDateFormat(pattern, Locale.getDefault())


val dayMonthYearFormat = dateFormat("dd/MM/yyyy")

const val DEFAULT_DECIMAL_FORMAT = "%.2f"
package com.elian.computeit.core.util.constants

import java.text.SimpleDateFormat
import java.util.*

private fun dateFormat(pattern: String) = SimpleDateFormat(pattern, Locale.getDefault())


val profileDateFormat = dateFormat("dd/MM/yyyy")
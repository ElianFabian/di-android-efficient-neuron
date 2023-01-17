package com.elian.computeit.core.domain.models

import com.elian.computeit.core.util.Error
import com.elian.computeit.core.util.SimpleResource

data class TestConfigurationResult(
	val startOfRangeError: Error? = null,
	val endOfRangeError: Error? = null,
	val timeError: Error? = null,
	val resource: SimpleResource? = null,
)
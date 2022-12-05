package com.elian.computeit.feature_tests.domain.model

import com.elian.computeit.core.util.Error
import com.elian.computeit.core.util.SimpleResource

data class TestConfigurationResult(
	val startError: Error? = null,
	val endError: Error? = null,
	val timeError: Error? = null,
	val resource: SimpleResource? = null,
)
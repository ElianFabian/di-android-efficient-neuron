package com.elian.computeit.feature_tests.domain.models

import com.elian.computeit.core.util.Error
import com.elian.computeit.core.util.SimpleResource

data class TestConfigurationResult(
    val minValueError: Error? = null,
    val maxValueError: Error? = null,
    val timeError: Error? = null,
    val resource: SimpleResource? = null,
)
package com.elian.computeit.feature_tests.domain.models

import com.elian.computeit.core.util.SimpleResource
import com.elian.computeit.feature_tests.presentation.util.TestConfigurationError

data class TestConfigurationResult(
    val minValueError: TestConfigurationError? = null,
    val maxValueError: TestConfigurationError? = null,
    val timeError: TestConfigurationError? = null,
    val resource: SimpleResource? = null,
)
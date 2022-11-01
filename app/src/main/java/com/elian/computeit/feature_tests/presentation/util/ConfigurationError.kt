package com.elian.computeit.feature_tests.presentation.util

import com.elian.computeit.core.util.Error

sealed class ConfigurationError : Error()
{
    object ValueEmpty : ConfigurationError()
    object RangeValuesAreInverted : ConfigurationError()
}
package com.elian.computeit.feature_tests.domain.use_case

import com.elian.computeit.R
import com.elian.computeit.core.domain.util.checkErrors
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText.*
import com.elian.computeit.feature_tests.domain.models.TestConfigurationResult
import com.elian.computeit.feature_tests.presentation.util.TestConfigurationError
import javax.inject.Inject

class ValidateFieldsUseCase @Inject constructor()
{
    operator fun invoke(
        minValue: Int?,
        maxValue: Int?,
        testTime: Int?,
    ): TestConfigurationResult
    {
        val minValueError = getFieldError(minValue)
        val maxValueError = getFieldError(maxValue)
        val testTimeError = getFieldError(testTime)

        if (checkErrors(minValueError, maxValueError, testTimeError))
        {
            return TestConfigurationResult(
                minValueError = minValueError,
                maxValueError = maxValueError,
                testTimeError = testTimeError,
            )
        }
        if (minValue!! > maxValue!!)
        {
            return TestConfigurationResult(
                result = Resource.Error(StringResource(R.string.error_range_values_are_inverted))
            )
        }
        return TestConfigurationResult(result = Resource.Success())
    }
}

private fun getFieldError(number: Int?) = when (number)
{
    null -> TestConfigurationError.Empty
    else -> null
}
package com.elian.computeit.feature_tests.domain.use_case

import com.elian.computeit.R
import com.elian.computeit.core.domain.util.checkIfError
import com.elian.computeit.core.util.Resource
import com.elian.computeit.feature_tests.domain.models.TestConfigurationResult
import com.elian.computeit.feature_tests.presentation.util.TestConfigurationError
import javax.inject.Inject

class ValidateConfigurationFieldsUseCase @Inject constructor()
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

        return if (checkIfError(minValueError, maxValueError, testTimeError))
        {
            TestConfigurationResult(
                minValueError = minValueError,
                maxValueError = maxValueError,
                testTimeError = testTimeError,
            )
        }
        else if (minValue!! > maxValue!!)
        {
            TestConfigurationResult(result = Resource.Error(R.string.error_range_values_are_inverted))
        }
        else TestConfigurationResult(result = Resource.Success())
    }
}

private fun getFieldError(number: Int?) = when (number)
{
    null -> TestConfigurationError.Empty
    else -> null
}
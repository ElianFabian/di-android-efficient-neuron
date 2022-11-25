package com.elian.computeit.feature_tests.domain.use_case

import com.elian.computeit.R
import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.domain.util.checkIfError
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.getDivisiblePairsInRange
import com.elian.computeit.feature_tests.domain.models.TestConfigurationResult
import com.elian.computeit.feature_tests.presentation.util.TestConfigurationError
import javax.inject.Inject

class ValidateConfigurationFormUseCase @Inject constructor()
{
    private val _minRangeLength = 10
    private val _minDivisiblePairCount = 10


    operator fun invoke(
        operation: Operation,
        minValue: Int?,
        maxValue: Int?,
        time: Int?,
    ): TestConfigurationResult
    {
        val minValueError = getFieldError(minValue)
        val maxValueError = getFieldError(maxValue)
        val timeError = getFieldError(time)

        val divisiblePairCount = getDivisiblePairsInRange((minValue ?: 0)..(maxValue ?: 0)).size

        return when
        {
            checkIfError(minValueError, maxValueError, timeError)                        ->
            {
                TestConfigurationResult(
                    minValueError = minValueError,
                    maxValueError = maxValueError,
                    timeError = timeError,
                )
            }
            minValue!! > maxValue!!                                                      ->
            {
                TestConfigurationResult(result = Resource.Error(R.string.error_range_values_are_inverted))
            }
            maxValue - minValue + 1 < _minRangeLength                                    ->
            {
                TestConfigurationResult(result = Resource.Error(
                    messageResId = R.string.error_range_length_must_be_greater_than,
                    args = arrayOf(_minRangeLength)
                ))
            }
            operation == Operation.DIVIDE && divisiblePairCount < _minDivisiblePairCount ->
            {
                TestConfigurationResult(result = Resource.Error(
                    messageResId = R.string.error_range_not_enough_divisible_pairs,
                    args = arrayOf(divisiblePairCount, _minDivisiblePairCount)
                ))
            }
            else                                                                         ->
            {
                TestConfigurationResult(result = Resource.Success())
            }
        }
    }
}

private fun getFieldError(number: Int?) = when (number)
{
    null -> TestConfigurationError.Empty
    else -> null
}
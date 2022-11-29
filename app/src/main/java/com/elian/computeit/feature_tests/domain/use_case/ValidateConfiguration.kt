package com.elian.computeit.feature_tests.domain.use_case

import com.elian.computeit.R
import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.domain.states.NumericFieldError
import com.elian.computeit.core.domain.util.checkIfError
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.getDivisiblePairsInRange
import com.elian.computeit.feature_tests.domain.model.TestConfigurationResult
import javax.inject.Inject

class ValidateConfiguration @Inject constructor()
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
				TestConfigurationResult(resource = Resource.Error(R.string.error_range_values_are_inverted))
			}
			maxValue - minValue + 1 < _minRangeLength                                    ->
			{
				TestConfigurationResult(resource = Resource.Error(
					messageResId = R.string.error_range_length_must_be_greater_than,
					args = arrayOf(_minRangeLength)
				))
			}
			operation == Operation.DIVIDE && divisiblePairCount < _minDivisiblePairCount ->
			{
				TestConfigurationResult(resource = Resource.Error(
					messageResId = R.string.error_range_not_enough_divisible_pairs,
					args = arrayOf(divisiblePairCount, _minDivisiblePairCount)
				))
			}
			else                                                                         ->
			{
				TestConfigurationResult(resource = Resource.Success())
			}
		}
	}
}

private fun getFieldError(number: Int?) = when (number)
{
	null -> NumericFieldError.Empty
	else -> null
}
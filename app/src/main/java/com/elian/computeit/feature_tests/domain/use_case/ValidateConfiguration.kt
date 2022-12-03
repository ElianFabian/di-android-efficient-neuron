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

		if (checkIfError(minValueError, maxValueError, timeError))
		{
			return TestConfigurationResult(
				minValueError = minValueError,
				maxValueError = maxValueError,
				timeError = timeError,
			)
		}
		if (minValue!! > maxValue!!)
		{
			return TestConfigurationResult(resource = Resource.Error(R.string.error_range_values_are_inverted))
		}
		if (maxValue - minValue + 1 < _minRangeLength)
		{
			return TestConfigurationResult(resource = Resource.Error(
				messageResId = R.string.error_range_length_must_be_greater_than,
				args = arrayOf(_minRangeLength)
			))
		}
		if (operation == Operation.Division)
		{
			if (minValue == 0)
			{
				return TestConfigurationResult(resource = Resource.Error(R.string.error_division_by_zero_is_not_allowed))
			}

			getDivisiblePairsInRange(minValue, maxValue, ignoreSelfDivision = true).size.also()
			{
				if (it < _minDivisiblePairCount)
				{
					return TestConfigurationResult(resource = Resource.Error(
						messageResId = R.string.error_range_not_enough_divisible_pairs,
						args = arrayOf(it, _minDivisiblePairCount)
					))
				}
			}
		}

		return TestConfigurationResult(resource = Resource.Success())
	}
}

private fun getFieldError(number: Int?) = when (number)
{
	null -> NumericFieldError.Empty
	else -> null
}
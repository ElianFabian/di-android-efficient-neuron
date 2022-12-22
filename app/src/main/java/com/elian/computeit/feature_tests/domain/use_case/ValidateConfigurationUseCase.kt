package com.elian.computeit.feature_tests.domain.use_case

import com.elian.computeit.R
import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.domain.states.NumericFieldError
import com.elian.computeit.core.domain.util.checkIfError
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.getDivisiblePairsInRange
import com.elian.computeit.feature_tests.domain.model.TestConfigurationResult
import com.elian.computeit.feature_tests.domain.params.ValidateConfigurationParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ValidateConfigurationUseCase @Inject constructor()
{
	private val _minRangeLength = 10
	private val _minDivisiblePairCount = 10


	suspend operator fun invoke(params: ValidateConfigurationParams): TestConfigurationResult
	{
		val startError = getFieldError(params.start)
		val endError = getFieldError(params.end)
		val timeError = getFieldError(params.time)

		if (checkIfError(startError, endError, timeError))
		{
			return TestConfigurationResult(
				startError = startError,
				endError = endError,
				timeError = timeError,
			)
		}
		if (params.start!! > params.end!!)
		{
			return TestConfigurationResult(resource = Resource.Error(R.string.error_range_values_are_inverted))
		}
		if (params.end - params.start + 1 < _minRangeLength)
		{
			return TestConfigurationResult(resource = Resource.Error(
				messageResId = R.string.error_range_length_must_be_greater_than,
				args = arrayOf(_minRangeLength)
			))
		}
		if (params.operation == Operation.Division) withContext(Dispatchers.Default)
		{
			if (params.start == 0) return@withContext TestConfigurationResult(resource = Resource.Error(R.string.error_division_by_zero_is_not_allowed))

			getDivisiblePairsInRange(
				start = params.start,
				end = params.end,
				ignoreSelfDivision = true,
			).size.also()
			{
				if (it < _minDivisiblePairCount)
				{
					return@withContext TestConfigurationResult(resource = Resource.Error(
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
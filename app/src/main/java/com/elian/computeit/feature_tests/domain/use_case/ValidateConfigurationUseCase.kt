package com.elian.computeit.feature_tests.domain.use_case

import com.elian.computeit.R
import com.elian.computeit.core.domain.models.OperationType
import com.elian.computeit.core.domain.errors.NumericFieldError
import com.elian.computeit.core.domain.util.checkIfError
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.getDivisiblePairsInRangeCount
import com.elian.computeit.core.domain.models.TestConfigurationResult
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
		val startError = getFieldError(params.startOfRange)
		val endError = getFieldError(params.endOfRange)
		val timeError = getFieldError(params.time)

		if (checkIfError(startError, endError, timeError))
		{
			return TestConfigurationResult(
				startOfRangeError = startError,
				endOfRangeError = endError,
				timeError = timeError,
			)
		}
		if (params.startOfRange!! > params.endOfRange!!)
		{
			return TestConfigurationResult(resource = Resource.Error(R.string.error_range_values_are_inverted))
		}
		if (params.endOfRange - params.startOfRange + 1 < _minRangeLength)
		{
			return TestConfigurationResult(resource = Resource.Error(
				messageResId = R.string.error_range_length_must_be_greater_than,
				args = arrayOf(_minRangeLength)
			))
		}
		if (params.operation == OperationType.Division) withContext(Dispatchers.Default)
		{
			if (params.startOfRange == 0) return@withContext TestConfigurationResult(resource = Resource.Error(R.string.error_division_by_zero_is_not_allowed))

			getDivisiblePairsInRangeCount(
				start = params.startOfRange,
				end = params.endOfRange,
				ignoreSelfDivision = true,
			).also()
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
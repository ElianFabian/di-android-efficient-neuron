package com.elian.computeit.feature_tests.domain.use_case

import com.elian.computeit.R
import com.elian.computeit.core.domain.errors.NumericFieldError
import com.elian.computeit.core.domain.models.OperationType
import com.elian.computeit.core.domain.models.TestConfigurationResult
import com.elian.computeit.core.domain.models.TestConfigurationResultMessage
import com.elian.computeit.core.domain.util.checkIfError
import com.elian.computeit.core.util.Resource
import com.elian.computeit.core.util.UiText
import com.elian.computeit.core.util.getAllDivisiblePairsInRangeCount
import com.elian.computeit.feature_tests.domain.params.ValidateConfigurationParams
import javax.inject.Inject

class ValidateConfigurationUseCase @Inject constructor() {

	operator fun invoke(params: ValidateConfigurationParams): TestConfigurationResult {
		val startError = getFieldError(params.startOfRange)
		val endError = getFieldError(params.endOfRange)
		val timeError = getFieldError(params.timeInSeconds)

		if (checkIfError(startError, endError, timeError)) {
			return TestConfigurationResult(
				startOfRangeError = startError,
				endOfRangeError = endError,
				timeError = timeError,
			)
		}

		val messageInfo = when {
			params.startOfRange!! > params.endOfRange!!                            -> {
				TestConfigurationResultMessage.RangeValuesAreInverted(UiText(R.string.error_range_values_are_inverted))
			}
			params.endOfRange - params.startOfRange + 1 < MinRangeLength           -> {
				TestConfigurationResultMessage(
					UiText(
						resId = R.string.error_range_length_must_be_greater_than,
						args = arrayOf(MinRangeLength)
					)
				)
			}
			params.operation == OperationType.Division && params.startOfRange == 0 -> {
				TestConfigurationResultMessage(UiText(R.string.error_division_by_zero_is_not_allowed))
			}
			params.operation == OperationType.Division                             -> {

				val divisiblePairsCount = getAllDivisiblePairsInRangeCount(
					start = params.startOfRange,
					end = params.endOfRange,
					ignoreSelfDivision = true,
				)

				if (divisiblePairsCount < MinDivisiblePairCount) {
					TestConfigurationResultMessage(
						UiText(
							resId = R.string.error_range_not_enough_divisible_pairs,
							args = arrayOf(
								divisiblePairsCount,
								MinDivisiblePairCount,
							)
						)
					)
				}
				else null
			}
			else                                                                   -> null
		}

		if (messageInfo != null) {
			return TestConfigurationResult(
				messageInfo = messageInfo,
				resource = Resource.Error(messageInfo.content)
			)
		}

		return TestConfigurationResult(resource = Resource.Success())
	}


	companion object {
		private const val MinRangeLength = 10
		private const val MinDivisiblePairCount = 10
	}
}


private fun getFieldError(number: Int?) = when (number) {
	null -> NumericFieldError.Empty
	else -> null
}
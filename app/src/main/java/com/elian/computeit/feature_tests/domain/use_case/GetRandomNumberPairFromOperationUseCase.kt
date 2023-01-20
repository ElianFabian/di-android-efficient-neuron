package com.elian.computeit.feature_tests.domain.use_case

import com.elian.computeit.core.domain.models.NumberPair
import com.elian.computeit.core.domain.models.OperationType
import com.elian.computeit.core.util.getAllDivisiblePairsInRange
import javax.inject.Inject
import kotlin.random.Random

class GetRandomNumberPairFromOperationUseCase @Inject constructor()
{
	private val _randomSeed = Random(System.currentTimeMillis())

	private lateinit var _previousRange: IntRange
	private lateinit var _divisiblePairs: List<NumberPair>


	/**
	 * Returns a random number pair.
	 * @param oldPair if isn't null the returned pair will always be different from the old one.
	 */
	operator fun invoke(
		operation: OperationType,
		range: IntRange,
		oldPair: NumberPair? = null,
	): NumberPair
	{
		if (oldPair == null) return getRandomPairFromCurrentOperationInRange(operation, range)

		while (true)
		{
			val newPair = getRandomPairFromCurrentOperationInRange(operation, range)

			val arePairsDifferent = newPair.first != oldPair.first || newPair.second != oldPair.second
			val arePairsDifferentInReverse = newPair.first != oldPair.second || newPair.second != oldPair.first

			if (arePairsDifferent && arePairsDifferentInReverse) return newPair
		}
	}


	private fun getRandomPairFromCurrentOperationInRange(
		operation: OperationType,
		range: IntRange,
	): NumberPair
	{
		if (!::_divisiblePairs.isInitialized)
		{
			_divisiblePairs = getDivisiblePairs(operation, range)

			_previousRange = range
		}
		else if (range != _previousRange)
		{
			_divisiblePairs = getDivisiblePairs(operation, range)
		}

		return when (operation)
		{
			OperationType.Addition       -> getRandomPairExceptIf(range) { it.first == 0 && it.second == 0 }
			OperationType.Subtraction    -> getRandomPairExceptIf(range) { it.first == it.second }
			OperationType.Multiplication -> getRandomPairExceptIf(range) { it.first == 0 || it.second == 0 }
			OperationType.Division       -> _divisiblePairs.random(_randomSeed)
		}
	}

	private fun getRandomPairExceptIf(
		range: IntRange,
		condition: (pair: NumberPair) -> Boolean,
	): NumberPair
	{
		while (true)
		{
			val newPair = getRandomNumberPair(range)

			if (condition(newPair)) continue

			return newPair
		}
	}

	private fun getRandomNumberPair(range: IntRange): NumberPair
	{
		val first = range.random(_randomSeed)
		val second = range.random(_randomSeed)

		return NumberPair(first, second)
	}

	private fun getDivisiblePairs(
		operation: OperationType,
		range: IntRange,
	): List<NumberPair>
	{
		return if (operation == OperationType.Division)
		{
			getAllDivisiblePairsInRange(
				start = range.first,
				end = range.last,
				ignoreSelfDivision = true,
			).map { NumberPair(it[0], it[1]) }
		}
		else emptyList()
	}
}